package books_management.api.controller;

import books_management.api.dto.common.BaseResponse;
import books_management.api.dto.create_book.request.CreateBookRequest;
import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.dto.get_all_book.response.PageResponse;
import books_management.api.repository.BooksRepository;
import books_management.api.service.BooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class BooksControllerIT  {
    @LocalServerPort
    private int port;
    @Autowired
    private BooksRepository bookRepository;
    @Autowired
    private BooksService booksService;
    @Autowired
    private TestRestTemplate restTemplate;
    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("initdb/init.sql");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);

        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQL8Dialect");

    }
    @BeforeEach
    void setup() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Duplicate Book");
        request.setAuthor("Author");
        request.setPublishedDate("2566-06-15");
        request.setCreatedBy("tester");
        booksService.createBook(request);
    }
    @Test
    void testGetBooksByAuthor() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Duplicate Book");
        request.setAuthor("Author");
        request.setPublishedDate("2566-06-15");
        request.setCreatedBy("tester");
        booksService.createBook(request);
        String author = "Author";
        String url = "http://localhost:" + port + "/v1/books?author=" +
                URLEncoder.encode(author, StandardCharsets.UTF_8);

        ResponseEntity<BaseResponse<PageResponse<GetAllBooksResponse>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<PageResponse<GetAllBooksResponse>>>(){});
                assertTrue(response.getBody().isStatus());
                assertEquals("Author", response.getBody().getData().content().get(0).getAuthor());
                assertEquals("Duplicate Book", response.getBody().getData().content().get(0).getTitle());
    }

    @Test
    void testGetBooksByAuthorButEmpty() {

        String author = "Author2";
        String url = "http://localhost:" + port + "/v1/books?author=" +
                URLEncoder.encode(author, StandardCharsets.UTF_8);

        ResponseEntity<BaseResponse<PageResponse<GetAllBooksResponse>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<PageResponse<GetAllBooksResponse>>>(){});
        assertTrue(response.getBody().isStatus());
        assertNull(response.getBody().getError());
        assertTrue(response.getBody().getData().content().isEmpty());

    }

    @Test
    void testCreateBook() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("New Book");
        request.setAuthor("New Author");
        request.setPublishedDate("2566-06-15");
        request.setCreatedBy("tester");
        String url = "http://localhost:" + port + "/v1/books";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateBookRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<BaseResponse<String>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<BaseResponse<String>>(){});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isStatus());
        assertEquals("Book created successfully", response.getBody().getData());
    }


    @Test
    void testCreateBookDuplicate() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Duplicate Book");
        request.setAuthor("Author");
        request.setPublishedDate("2566-06-15");
        request.setCreatedBy("tester");
        String url = "http://localhost:" + port + "/v1/books";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateBookRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<BaseResponse<String>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<BaseResponse<String>>(){});
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertFalse(response.getBody().isStatus());
        assertEquals("Book already exists", response.getBody().getError());
    }

    @Test
    void testCreateBookWithInvalidDate() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Invalid Date Book");
        request.setAuthor("Author");
        request.setPublishedDate("invalid-date");
        request.setCreatedBy("tester");
        String url = "http://localhost:" + port + "/v1/books";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateBookRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<BaseResponse<String>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<BaseResponse<String>>(){});
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isStatus());
        assertNotNull(response.getBody().getError());
    }

    @Test
    void testCreateBookWithEmptyTitle() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("");
        request.setAuthor("Author");
        request.setPublishedDate("2566-06-15");
        request.setCreatedBy("tester");
        String url = "http://localhost:" + port + "/v1/books";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateBookRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<BaseResponse<String>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<BaseResponse<String>>(){});
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isStatus());
        assertNotNull(response.getBody().getError());
    }

    @Test
    void testGetBooksByAuthorWithInvalidPageSize() {
        String author = "Author";
        String url = "http://localhost:" + port + "/v1/books?author=" +
                URLEncoder.encode(author, StandardCharsets.UTF_8) + "&page=0&size=11";

        ResponseEntity<BaseResponse<PageResponse<GetAllBooksResponse>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<PageResponse<GetAllBooksResponse>>>(){});
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isStatus());
        assertNotNull(response.getBody().getError());
    }

    @Test
    void testGetBooksByAuthorWithInvalidPageNumber() {
        String author = "Author";
        String url = "http://localhost:" + port + "/v1/books?author=" +
                URLEncoder.encode(author, StandardCharsets.UTF_8) + "&page=-1&size=5";

        ResponseEntity<BaseResponse<PageResponse<GetAllBooksResponse>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<PageResponse<GetAllBooksResponse>>>(){});
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isStatus());
        assertNotNull(response.getBody().getError());
    }

}