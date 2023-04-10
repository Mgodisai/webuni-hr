package hu.webuni.hr.alagi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorEntity> handleValidationError(MethodArgumentNotValidException exception) {
        log.warn(exception.getMessage(), exception);
        List<FieldErrorDto> fieldErrorDtoList =
              exception.getBindingResult().getFieldErrors()
                    .stream()
                    .map(fieldError->new FieldErrorDto(fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()))
                    .toList();
        return ResponseEntity.badRequest()
                .body(new ValidationErrorEntity(
                        1000,
                        exception.getMessage(),
                        fieldErrorDtoList
                        )
                );
    }

   @ExceptionHandler(EntityAlreadyExistsWithGivenIdException.class)
   public ResponseEntity<DefaultErrorEntity> handleEmployeeIsAlreadyExistsException(EntityAlreadyExistsWithGivenIdException exception) {
      log.warn(exception.getMessage(), exception);

      return ResponseEntity.badRequest()
            .body(new DefaultErrorEntity(
                        1001,
                        exception.getMessage()
                  )
            );
   }

   @ExceptionHandler(EntityNotExistsWithGivenIdException.class)
   public ResponseEntity<DefaultErrorEntity> handleEmployeeNotExistsByGivenIdException(EntityNotExistsWithGivenIdException exception) {
      log.warn(exception.getMessage(), exception);

      return ResponseEntity.badRequest()
            .body(new DefaultErrorEntity(
                        1002,
                        exception.getMessage()
                  )
            );
   }

   @ExceptionHandler(EmployeeNotBelongsToTheGivenCompanyException.class)
   public ResponseEntity<DefaultErrorEntity> handleEmployeeNotBelongsToTheGivenCompanyException(EmployeeNotBelongsToTheGivenCompanyException exception) {
      log.warn(exception.getMessage(), exception);

      return ResponseEntity.badRequest()
            .body(new DefaultErrorEntity(
                        1003,
                        exception.getMessage()
                  )
            );
   }
}
