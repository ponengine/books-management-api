package books_management.api.service;

import books_management.api.dto.create_book.request.CreateBookRequest;
import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.repository.BooksRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class BooksServiceImplTest {

    @Mock
    private BooksRepository booksRepository;

    @Inject
    private BooksServiceImpl booksService;
    @Test
    void getAllBooksByAuthor_whenAuthorNameIsEmpty_returnsBadRequest() {
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);

        var response = service.getAllBooksByAuthor("", 0, 5);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isStatus());
        assertEquals("Author name is required", response.getBody().getError());
    }

    @Test
    void getAllBooksByAuthor_whenAuthorNameIsLessThan3_returnsBadRequest() {
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);

        var response = service.getAllBooksByAuthor("ab", 0, 5);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isStatus());
        assertEquals("Author name must be at least 3 characters long", response.getBody().getError());
    }

    @Test
    void getAllBooksByAuthor_whenSuccess_returnsBooks() {
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);
        List<GetAllBooksResponse> books =List.of(new GetAllBooksResponse());
        Pageable pageable = Pageable.ofSize(5).withPage(0);
        Page<GetAllBooksResponse> page = new PageImpl<>(books, pageable, books.size());
        Mockito.when(booksRepository.findByAuthorName("John Doe",pageable)).thenReturn(page);

        var response = service.getAllBooksByAuthor("John Doe", 0, 5);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isStatus());
        assertEquals(books, response.getBody().getData().content());
    }

    @Test
    void getAllBooksByAuthor_whenSuccessNoData_returnsEmptyList() {
        Pageable pageable = Pageable.ofSize(5).withPage(0);
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);
        Page<GetAllBooksResponse> page = new PageImpl<>(Collections.emptyList(), pageable, Collections.emptyList().size());
        Mockito.when(booksRepository.findByAuthorName("Jane Doe",pageable)).thenReturn(page);

        var response = service.getAllBooksByAuthor("Jane Doe", 0, 5);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isStatus());
        assertTrue(response.getBody().getData().content().isEmpty());
    }

    @Test
    void getAllBooksByAuthor_whenExceptionThrown_throwsException() {
        Pageable pageable = Pageable.ofSize(5).withPage(0);
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);
        Mockito.when(booksRepository.findByAuthorName("Error",pageable)).thenThrow(new RuntimeException("DB error"));
        assertThrows(RuntimeException.class, () -> service.getAllBooksByAuthor("Error", 0, 5));
    }


    @Test
    void createBook_whenExceptionThrown_throwsException() {
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Test Book");
        Mockito.when(booksRepository.existsByTitle("Test Book")).thenThrow(new RuntimeException("DB error"));
        assertThrows(RuntimeException.class, () -> service.createBook(request));
    }

    @Test
    void createBook_whenDuplicateFound_returnsConflict() {
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Duplicate Book");
        Mockito.when(booksRepository.existsByTitle("Duplicate Book")).thenReturn(true);

        var response = service.createBook(request);
        assertEquals(409, response.getStatusCodeValue());
        assertFalse(response.getBody().isStatus());
        assertEquals("Book already exists", response.getBody().getError());
    }

    @Test
    void createBook_whenSuccess_returnsOk() {
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("New Book");
        request.setAuthor("Author");
        request.setPublishedDate("2567-01-01");
        request.setCreatedBy("admin");
        Mockito.when(booksRepository.existsByTitle("New Book")).thenReturn(false);

        var response = service.createBook(request);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isStatus());
        assertEquals("Book created successfully", response.getBody().getData());
    }

    @Test
    void pageSizeValidation_whenPageIsNegative() {
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getAllBooksByAuthor("David",-1, 5);
        });

        assertEquals("Page number must be non-negative and size must be positive", exception.getMessage());
    }

    @Test
    void pageSizeValidation_whenSizeIsZero() {
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getAllBooksByAuthor("David", 0, 0);
        });

        assertEquals("Page number must be non-negative and size must be positive", exception.getMessage());
    }

    @Test
    void createBook_whenWrongDate_returnError() {
        booksRepository = Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("New Book");
        request.setAuthor("Author");
        request.setPublishedDate("256-01-01");
        request.setCreatedBy("admin");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.createBook(request);
        });
        assertEquals("Year must be in the Thai Buddhist calendar (e.g. 2568)", exception.getMessage());
    }
}