package li.yuhang.fogofworld.server.security;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.annotation.AccessType;

import javax.persistence.Access;

@Getter
@Setter
@Configuration
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "security")
public class SecuritySettings {

    private String secretKey;
    private long expireLength;
    private String signUpPath;
    private String loginPath;

    public final String TOKEN_PREFIX = "Bearer ";
    public final String AUTH_HEADER_STRING = "Authorization";

}
