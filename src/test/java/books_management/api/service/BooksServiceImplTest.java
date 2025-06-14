package books_management.api.service;

import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.repository.BooksRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        BooksRepository booksRepository = org.mockito.Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);

        var response = service.getAllBooksByAuthor("");
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isStatus());
        assertEquals("Author name is required", response.getBody().getError());
    }

    @Test
    void getAllBooksByAuthor_whenAuthorNameIsLessThan3_returnsBadRequest() {
        BooksRepository booksRepository = org.mockito.Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);

        var response = service.getAllBooksByAuthor("ab");
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isStatus());
        assertEquals("Author name must be at least 3 characters long", response.getBody().getError());
    }

    @Test
    void getAllBooksByAuthor_whenSuccess_returnsBooks() {
        BooksRepository booksRepository = org.mockito.Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);
        List<GetAllBooksResponse> books = java.util.List.of(new GetAllBooksResponse());
        org.mockito.Mockito.when(booksRepository.findByAuthorName("John Doe")).thenReturn(books);

        var response = service.getAllBooksByAuthor("John Doe");
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isStatus());
        assertEquals(books, response.getBody().getData());
    }

    @Test
    void getAllBooksByAuthor_whenSuccessNoData_returnsEmptyList() {
        BooksRepository booksRepository = org.mockito.Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);
        org.mockito.Mockito.when(booksRepository.findByAuthorName("Jane Doe")).thenReturn(java.util.Collections.emptyList());

        var response = service.getAllBooksByAuthor("Jane Doe");
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isStatus());
        assertTrue(response.getBody().getData().isEmpty());
    }

    @Test
    void getAllBooksByAuthor_whenExceptionThrown_throwsException() {
        BooksRepository booksRepository = org.mockito.Mockito.mock(BooksRepository.class);
        BooksServiceImpl service = new BooksServiceImpl(booksRepository);
        org.mockito.Mockito.when(booksRepository.findByAuthorName("Error")).thenThrow(new RuntimeException("DB error"));
        assertThrows(RuntimeException.class, () -> service.getAllBooksByAuthor("Error"));
    }
}