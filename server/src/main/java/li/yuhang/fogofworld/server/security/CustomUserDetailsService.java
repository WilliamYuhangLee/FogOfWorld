package li.yuhang.fogofworld.server.security;

import li.yuhang.fogofworld.server.exception.ApiExceptionFactory;
import li.yuhang.fogofworld.server.exception.EntityNotFoundException;
import li.yuhang.fogofworld.server.model.Account;
import li.yuhang.fogofworld.server.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(
            ApiExceptionFactory.formatMessage(Account.class, EntityNotFoundException.class, username));
        }
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(account.getRole().name());
        return new User(username, account.getPassword(), Collections.singleton(grantedAuthority));
    }

}
