package li.yuhang.fogofworld.server.exception;

import li.yuhang.fogofworld.server.config.ExceptionPropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class ApiExceptionFactory {

    private static ExceptionPropertiesConfiguration exceptionPropertiesConfiguration;

    @Autowired
    public ApiExceptionFactory(ExceptionPropertiesConfiguration exceptionPropertiesConfiguration) {
        ApiExceptionFactory.exceptionPropertiesConfiguration = exceptionPropertiesConfiguration;
    }

    static abstract class ApiException extends RuntimeException {
        ApiException(String message) {
            super(message);
        }
    }

    public static <E extends ApiException> ApiException exception(Class entityClass, Class<E> exceptionClass, String... args) {
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
