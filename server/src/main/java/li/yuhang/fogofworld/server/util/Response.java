package li.yuhang.fogofworld.server.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {

    public enum Status {
        OK,
        BAD_REQUEST, UNAUTHORIZED, VALIDATION_EXCEPTION, EXCEPTION, WRONG_CREDENTIALS, ACCESS_DENIED, NOT_FOUND, DUPLICATE_ENTITY
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @NoArgsConstructor
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Error {
        private Date timestamp;
        private String message;
        private String details;
    }

    private Status status;
    private T payload;
    private Object error;
    private Object metadata;

    public static <T> Response<T> withStatus(Status status) {
        Response<T> response = new Response<>();
        response.setStatus(status);
        return response;
    }

    public void addErrorMessage(String message, Exception exception) {
        this.setError(new Error()
                      .setDetails(message)
                      .setMessage(exception.getMessage())
                      .setTimestamp(new Date()));
    }
}
