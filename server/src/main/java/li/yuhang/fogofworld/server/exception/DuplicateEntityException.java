package li.yuhang.fogofworld.server.exception;

public class DuplicateEntityException extends ApiExceptionFactory.ApiException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
