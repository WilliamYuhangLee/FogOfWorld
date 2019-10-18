package li.yuhang.fogofworld.server.exception;

import li.yuhang.fogofworld.server.util.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static <T extends Exception> ResponseEntity<Response> responseEntity(T e, HttpStatus status) {
        return new ResponseEntity<>(Response.withStatus(status).addException(e), status);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleException(RuntimeException e) {
        return responseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiExceptionFactory.ApiException.class)
    public ResponseEntity<Response> handleException(ApiExceptionFactory.ApiException e) {
        return responseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Response> handleException(EntityNotFoundException e) {
        return responseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Response> handleException(DuplicateEntityException e) {
        return responseEntity(e, HttpStatus.CONFLICT);
    }

}
