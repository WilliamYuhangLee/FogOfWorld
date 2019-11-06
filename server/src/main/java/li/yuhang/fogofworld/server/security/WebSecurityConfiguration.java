package li.yuhang.fogofworld.server.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity(debug = true)
public class WebSecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private final UserDetailsService accountService;

        private final SecuritySettings securitySettings;

        private final PasswordEncoder passwordEncoder;

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        private final JwtAuthorizationFilter jwtAuthorizationFilter;

        public ApiWebSecurityConfigurerAdapter(@Qualifier(value = "accountServiceImpl") UserDetailsService accountService,
                                               SecuritySettings securitySettings,
                                               PasswordEncoder passwordEncoder,
                                               @Lazy JwtAuthenticationFilter jwtAuthenticationFilter,
                                               @Lazy JwtAuthorizationFilter jwtAuthorizationFilter) {
            this.accountService = accountService;
            this.securitySettings = securitySettings;
            this.passwordEncoder = passwordEncoder;
            this.jwtAuthenticationFilter = jwtAuthenticationFilter;
            this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
        }

        @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors();
            http.csrf().disable();
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.antMatcher("/api/**").authorizeRequests()
                .antMatchers(HttpMethod.POST, securitySettings.getSignUpPath()).permitAll()
                .anyRequest().authenticated();
            http.addFilter(jwtAuthenticationFilter);
            http.addFilter(jwtAuthorizationFilter);
            http.exceptionHandling().authenticationEntryPoint((request, response, exception) ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage()));
        }
    }
}
