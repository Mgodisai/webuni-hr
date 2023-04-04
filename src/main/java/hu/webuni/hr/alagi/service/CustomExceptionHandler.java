package hu.webuni.hr.alagi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationError(MethodArgumentNotValidException exception) {
        log.warn(exception.getMessage(), exception);

        return ResponseEntity.badRequest()
                .body(new ValidationError(
                        1000,
                        exception.getMessage(),
                        exception.getBindingResult().getFieldErrors()
                        )
                );
    }
}
