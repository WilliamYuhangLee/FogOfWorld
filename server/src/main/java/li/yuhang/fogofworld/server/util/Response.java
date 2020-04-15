package li.yuhang.fogofworld.server.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {

    @Getter
    @Setter
    @Accessors(chain = true)
    @NoArgsConstructor
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class Error {
        private Date timestamp;
        private String message;
        private String details;
    }

    private HttpStatus status;
    private T payload;
    private Object error;
    private Object metadata;

    public static <T> Response<T> withStatus(HttpStatus status) {
        Response<T> response = new Response<>();
        response.setStatus(status);
        return response;
    }

    public Response<T> addErrorMessage(String message, Exception exception) {
        return this.setError(new Error()
                             .setDetails(message)
                             .setMessage(exception.getMessage())
                             .setTimestamp(new Date()));
    }

    public Response<T> addErrorMessage(String message) {
        return this.setError(new Error()
                .setDetails(message)
                .setMessage(message)
                .setTimestamp(new Date()));
    }

    public Response<T> addException(Exception e) {
        return this.addErrorMessage(e.getMessage(), e);
    }
}
