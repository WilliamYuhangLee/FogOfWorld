package li.yuhang.fogofworld.server.exception;

public class DuplicateEntityException extends APIExceptionFactory.APIException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
