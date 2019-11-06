package li.yuhang.fogofworld.server.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class CustomExceptions {

    private static ExceptionMessages exceptionMessages;

    @Autowired
    public CustomExceptions(ExceptionMessages exceptionMessages) {
        CustomExceptions.exceptionMessages = exceptionMessages;
    }

    static abstract class ApiException extends RuntimeException {
        ApiException(String message) {
            super(message);
        }
    }

    public static <E extends ApiException> E raise(Class<E> exceptionClass, Class entityClass, String... args) {
        try {
            return exceptionClass.getConstructor(String.class).newInstance(formatMessage(entityClass, exceptionClass, args));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String formatConfigKey(Class entityClass, Class exceptionClass) {
        return (entityClass.getSimpleName() + "." + exceptionClass.getSimpleName().replace("Exception", "")).toLowerCase();
    }

    public static String formatMessage(Class entityClass, Class exceptionClass, String... args) {
        String configKey = formatConfigKey(entityClass, exceptionClass);
        String messageTemplate = exceptionMessages.getConfigValue(configKey);
        return messageTemplate == null ? "An error has occurred." : MessageFormat.format(messageTemplate, args);
    }
}
