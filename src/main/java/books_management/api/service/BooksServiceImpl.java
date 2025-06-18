package books_management.api.service;

import books_management.api.dto.common.BaseResponse;
import books_management.api.dto.create_book.request.CreateBookRequest;
import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.dto.get_all_book.response.PageResponse;
import books_management.api.entity.Books;
import books_management.api.repository.BooksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class BooksServiceImpl implements  BooksService {
    private static final Logger logger = LoggerFactory.getLogger(BooksServiceImpl.class);
    private final BooksRepository booksRepository;

    public BooksServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponse<GetAllBooksResponse>>> getAllBooksByAuthor(String authorName, int page, int size) {
        var validationError = validateAuthorName(authorName);
        validatePageSize(page, size);
        Pageable pageable = PageRequest.of(page, size);
        if (validationError != null) {
            logger.error("Validation failed for authorName={}: {}", authorName, validationError);
            return ResponseEntity.badRequest().body(new BaseResponse<>(null, false, validationError));
        }
        try{
            Page<GetAllBooksResponse> pageBook=booksRepository.findByAuthorName(authorName,pageable);
            PageResponse<GetAllBooksResponse> pageResponse = new PageResponse<>(
                    pageBook.getContent(),
                    pageBook.getNumber(),
                    pageBook.getSize(),
                    pageBook.getTotalElements(),
                    pageBook.getTotalPages()
            );
            logger.info("Found {} books for authorName={}", pageResponse.totalElements(), authorName);
            return ResponseEntity.ok(new BaseResponse<>(pageResponse, true, null));
        }catch (Exception ex){
            logger.error("Error retrieving books by author: {}", ex.getMessage());
            throw ex;
        }
    }

    private void validatePageSize(int page, int size) {
        if (page < 0 || size <= 0) {
            String errorMessage = "Page number must be non-negative and size must be positive";
            logger.error("Validation failed: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (size > 10) {
            String errorMessage = "Size must not exceed 10";
            logger.error("Validation failed: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    public ResponseEntity<BaseResponse<String>> createBook(CreateBookRequest createBookRequest) {
        try{
            if (booksRepository.existsByTitle(createBookRequest.getTitle())) {
                logger.error("Book creation failed: duplicate title={}", createBookRequest.getTitle());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new BaseResponse<>(null, false, "Book already exists"));
            }
            var book = new Books().builder()
                    .title(createBookRequest.getTitle())
                    .author(createBookRequest.getAuthor())
                    .publishedDate(transformThaiYearToUSYear(createBookRequest.getPublishedDate()))
                    .isActive(true)
                    .createdBy(createBookRequest.getCreatedBy()).build();
            booksRepository.save(book);
            logger.info("Book created: title={}, author={}",
                    createBookRequest.getTitle(), createBookRequest.getAuthor());
            return ResponseEntity.ok(new BaseResponse<>("Book created successfully", true, null));
        }catch (Exception ex){
            logger.error("Error creating book: {}", ex.getMessage());
            throw ex;
        }
    }



    private Date transformThaiYearToUSYear(String thaiDateStr) {
        if (thaiDateStr == null || thaiDateStr.isEmpty()) return null;
        try {
            String[] parts = thaiDateStr.split("-");
            int thaiYear = Integer.parseInt(parts[0]);
            int usYear = thaiYear > 1000 ? thaiYear - 543 : thaiYear;
            var usDateStr = usYear + "-" + parts[1] + "-" + parts[2];
            return java.sql.Date.valueOf(usDateStr);
        } catch (Exception e) {
            logger.error("Error transforming date: {} → {}", thaiDateStr, e.getMessage());
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd.");
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
