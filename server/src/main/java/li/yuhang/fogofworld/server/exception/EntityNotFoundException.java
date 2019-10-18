package li.yuhang.fogofworld.server.exception;

public class EntityNotFoundException extends ApiExceptionFactory.ApiException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
