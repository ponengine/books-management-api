package books_management.api.controller;

import books_management.api.dto.common.BaseResponse;
import books_management.api.dto.create_book.request.CreateBookRequest;
import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.dto.get_all_book.response.PageResponse;
import books_management.api.service.BooksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1")
public class BooksController {
    private static final Logger logger = LoggerFactory.getLogger(BooksController.class);
    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @Operation(
            summary = "Get All Books by Author",
            description = "This endpoint retrieves all books written by a specific author.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of books",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid author name",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - An unexpected error occurred",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @GetMapping("/books")
    public ResponseEntity<BaseResponse<PageResponse<GetAllBooksResponse>>> getAllBooksByAuthor(
            @RequestParam String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        logger.info("Received request to get all books by author: {}, page: {}, size: {}", author, page, size);
        return booksService.getAllBooksByAuthor(author, page, size);
    }

    @Operation(
            summary = "Create a New Book",
            description = "This endpoint allows you to create a new book entry in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book created successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid book data",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - An unexpected error occurred",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @PostMapping("/books")
    public ResponseEntity<BaseResponse<String>> createBook(@RequestBody @Valid CreateBookRequest createBookRequest) {
        logger.info("Received request to create a new book: {}", createBookRequest);
        return booksService.createBook(createBookRequest);
    }

}
