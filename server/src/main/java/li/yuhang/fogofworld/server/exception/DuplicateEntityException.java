package li.yuhang.fogofworld.server.exception;

public class DuplicateEntityException extends CustomExceptions.ApiException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
