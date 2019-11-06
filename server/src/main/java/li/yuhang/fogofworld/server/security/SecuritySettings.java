package li.yuhang.fogofworld.server.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "security")
public class SecuritySettings {

    private String secretKey;
    private long expireLength;

}
