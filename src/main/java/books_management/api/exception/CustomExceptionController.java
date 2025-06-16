package books_management.api.exception;

import books_management.api.dto.common.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Hidden
@RestControllerAdvice
public class CustomExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        BaseResponse<String> response = new BaseResponse<>(
                null,
                false,
                errorMessages.toString()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleAllExceptions(Exception ex) {
        BaseResponse<String> response = new BaseResponse<>(
                null,
                false,
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<String>> handleIllegalArgumentExceptions(IllegalArgumentException ex) {
        BaseResponse<String> response = new BaseResponse<>(
                null,
                false,
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
