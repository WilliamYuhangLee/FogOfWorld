package li.yuhang.fogofworld.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import li.yuhang.fogofworld.server.request.AccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final SecuritySettings securitySettings;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtTokenProvider jwtTokenProvider,
                                   SecuritySettings securitySettings) {
        this.setAuthenticationManager(authenticationManager);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(securitySettings.getLoginPath(), "POST"));
        this.jwtTokenProvider = jwtTokenProvider;
        this.securitySettings = securitySettings;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AccountRequest requestBody = new ObjectMapper().readValue(request.getInputStream(), AccountRequest.class);
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                    requestBody.getUsername(),
                    requestBody.getPassword(),
                    new ArrayList<>()));
        } catch (IOException e) {
            throw new BadCredentialsException("Failed to deserialize request body while performing JWT authentication.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String token = jwtTokenProvider.createToken(((UserDetails) authResult.getPrincipal()).getUsername());
        response.addHeader(securitySettings.AUTH_HEADER_STRING, securitySettings.TOKEN_PREFIX + token);
    }
}
