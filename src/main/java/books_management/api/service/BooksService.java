package books_management.api.service;

import books_management.api.dto.common.BaseResponse;
import books_management.api.dto.create_book.request.CreateBookRequest;
import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.dto.get_all_book.response.PageResponse;
import org.springframework.http.ResponseEntity;


public interface BooksService {
    ResponseEntity<BaseResponse<PageResponse<GetAllBooksResponse>>> getAllBooksByAuthor(String authorName, int page, int size);
    ResponseEntity<BaseResponse<String>> createBook(CreateBookRequest createBookRequest);
}
