package books_management.api.service;

import books_management.api.dto.common.BaseResponse;
import books_management.api.dto.create_book.request.CreateBookRequest;
import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BooksService {
    ResponseEntity<BaseResponse<List<GetAllBooksResponse>>> getAllBooksByAuthor(String authorName);
    ResponseEntity<BaseResponse<String>> createBook(CreateBookRequest createBookRequest);
}
