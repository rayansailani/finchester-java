package kaif.finchesterjava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FieldAlreadyExist.class)
    public ResponseEntity<String> handleFieldAlreadyExistsException(FieldAlreadyExist ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<String> handleResouceNotFoundException(ResourceNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataNotSaved.class)
    public ResponseEntity<String> handleDataNotSavedException(ResourceNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CannotUpdate.class)
    public ResponseEntity<String> cannotUpdateException(ResourceNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_GATEWAY);
    }

}
