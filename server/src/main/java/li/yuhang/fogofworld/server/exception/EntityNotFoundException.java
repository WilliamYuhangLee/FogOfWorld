package li.yuhang.fogofworld.server.exception;

public class EntityNotFoundException extends APIExceptionFactory.APIException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
