package books_management.api.service;

import books_management.api.dto.common.BaseResponse;
import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.repository.BooksRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksServiceImpl implements  BooksService {

    private final BooksRepository booksRepository;

    public BooksServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public ResponseEntity<BaseResponse<List<GetAllBooksResponse>>> getAllBooksByAuthor(String authorName) {
        String validationError = validateAuthorName(authorName);
        if (validationError != null) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(null, false, validationError));
        }
        try{
            List<GetAllBooksResponse> books=booksRepository.findByAuthorName(authorName);
            return ResponseEntity.ok(new BaseResponse<>(books, true, null));
        }catch (Exception ex){
            throw ex;
        }
    }


    private String validateAuthorName(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            return "Author name is required";
        }
        if (authorName.length() < 3) {
            return "Author name must be at least 3 characters long";
        }
        return null;
    }

}
