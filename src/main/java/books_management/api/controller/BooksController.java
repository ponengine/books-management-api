package books_management.api.controller;

import books_management.api.dto.common.BaseResponse;
import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.service.BooksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class BooksController {

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
    public ResponseEntity<BaseResponse<List<GetAllBooksResponse>>> getAllBooksByAuthor(@RequestParam String author) {
        return booksService.getAllBooksByAuthor(author);
    }

}
