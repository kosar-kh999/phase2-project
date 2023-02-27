package ir.maktab.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExistException.class)
    public ResponseEntity<?> existExceptionHandler(ExistException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(NotCorrect.class)
    public ResponseEntity<?> notCorrectExceptionHandler(NotCorrect e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<?> notFoundExceptionHandler(NotFound e) {
        CustomException exception = new CustomException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(NotFoundUser.class)
    public ResponseEntity<?> notFoundUserExceptionHandler(NotFoundUser e) {
        CustomException exception = new CustomException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(OpinionException.class)
    public ResponseEntity<?> opinionExceptionHandler(OpinionException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<?> orderExceptionExceptionHandler(OrderException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(StatusException.class)
    public ResponseEntity<?> statusExceptionHandler(StatusException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(SubServicesException.class)
    public ResponseEntity<?> subServicesExceptionHandler(SubServicesException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(SuggestionException.class)
    public ResponseEntity<?> suggestionExceptionHandler(SuggestionException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validationExceptionHandler(ValidationException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(NotAccessException.class)
    public ResponseEntity<?> notAccessExceptionHandler(NotAccessException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }
}
