package books_management.api.service;

import books_management.api.dto.common.BaseResponse;
import books_management.api.dto.create_book.request.CreateBookRequest;
import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.repository.BooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Testcontainers
class BooksServiceImplIT {

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);

        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private BooksService booksService;
    @Autowired
    private BooksRepository booksRepository;
    @BeforeEach
    void cleanUp() {
        booksRepository.deleteAll();
    }
    @Test
    void shouldCreateBookSuccessfully() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Test Book");
        request.setAuthor("Test Author");
        request.setPublishedDate("2566-06-15");
        request.setCreatedBy("tester");

        ResponseEntity<BaseResponse<String>> response = booksService.createBook(request);

        assertNotNull(response);
        assertTrue(response.getBody().isStatus());
        assertEquals("Book created successfully", response.getBody().getData());
    }

    @Test
    void shouldGetBooksByAuthor() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Test Book 2");
        request.setAuthor("Test Author");
        request.setPublishedDate("2566-06-15");
        request.setCreatedBy("tester");
        booksService.createBook(request);

        ResponseEntity<BaseResponse<List<GetAllBooksResponse>>> response = booksService.getAllBooksByAuthor("Test Author");

        assertNotNull(response);
        assertTrue(response.getBody().isStatus());
        assertFalse(response.getBody().getData().isEmpty());
        assertEquals("Test Book 2", response.getBody().getData().get(0).getTitle());
    }

    @Test
    void shouldReturnErrorWhenBookAlreadyExists() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Duplicate Book");
        request.setAuthor("Author");
        request.setPublishedDate("2566-06-15");
        request.setCreatedBy("tester");

        booksService.createBook(request); // first time
        ResponseEntity<BaseResponse<String>> duplicateResponse = booksService.createBook(request); // second time

        assertNotNull(duplicateResponse);
        assertFalse(duplicateResponse.getBody().isStatus());
        assertEquals("Book already exists", duplicateResponse.getBody().getError());
    }

    @Test
    void shouldThrowExceptionWhenDateIsInvalid() {
        CreateBookRequest badDateRequest = new CreateBookRequest();
        badDateRequest.setTitle("Bad Date Book");
        badDateRequest.setAuthor("Test Author");
        badDateRequest.setPublishedDate("bad-date"); // wrong format
        badDateRequest.setCreatedBy("tester");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            booksService.createBook(badDateRequest);
        });

        assertEquals("Invalid date format. Expected yyyy-MM-dd.", exception.getMessage());
    }

    @Test
    void shouldReturnEmptyListWhenAuthorNotFound() {
        ResponseEntity<BaseResponse<List<GetAllBooksResponse>>> response =
                booksService.getAllBooksByAuthor("Unknown Author");

        assertNotNull(response);
        assertTrue(response.getBody().isStatus());
        assertTrue(response.getBody().getData().isEmpty());
    }
}