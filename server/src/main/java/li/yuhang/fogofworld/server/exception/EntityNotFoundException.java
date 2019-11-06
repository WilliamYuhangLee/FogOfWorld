package li.yuhang.fogofworld.server.exception;

public class EntityNotFoundException extends CustomExceptions.ApiException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
