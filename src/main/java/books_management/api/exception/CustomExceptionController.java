package books_management.api.exception;

import books_management.api.dto.common.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class CustomExceptionController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleAllExceptions(Exception ex) {
        BaseResponse<String> response = new BaseResponse<>(
                null,
                false,
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
