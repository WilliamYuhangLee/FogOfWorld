package li.yuhang.fogofworld.server.exception;

import li.yuhang.fogofworld.server.config.ExceptionPropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class APIExceptionFactory {

    private static ExceptionPropertiesConfiguration exceptionPropertiesConfiguration;

    @Autowired
    public APIExceptionFactory(ExceptionPropertiesConfiguration exceptionPropertiesConfiguration) {
        APIExceptionFactory.exceptionPropertiesConfiguration = exceptionPropertiesConfiguration;
    }

    static abstract class APIException extends RuntimeException {
        APIException(String message) {
            super(message);
        }
    }

    public static <E extends APIException> APIException exception(Class entityClass, Class<E> exceptionClass, String... args) {
        String configKey = formatConfigKey(entityClass, exceptionClass);
        String messageTemplate = exceptionPropertiesConfiguration.getConfigValue(configKey);
        String message = messageTemplate == null ? "An error has occurred." : MessageFormat.format(messageTemplate, args);
        try {
            return exceptionClass.getConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String formatConfigKey(Class entityClass, Class exceptionClass) {
        return (entityClass.getSimpleName() + "." + exceptionClass.getSimpleName().replace("Exception", "")).toLowerCase();
    }

}
