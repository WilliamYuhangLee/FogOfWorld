package li.yuhang.fogofworld.server.service;

import li.yuhang.fogofworld.server.dto.AccountDto;
import li.yuhang.fogofworld.server.exception.CustomExceptions;
import li.yuhang.fogofworld.server.exception.DuplicateEntityException;
import li.yuhang.fogofworld.server.exception.EntityNotFoundException;
import li.yuhang.fogofworld.server.model.Account;
import li.yuhang.fogofworld.server.dto.AccountMapper;
import li.yuhang.fogofworld.server.model.Role;
import li.yuhang.fogofworld.server.model.User;
import li.yuhang.fogofworld.server.repository.AccountRepository;
import li.yuhang.fogofworld.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository
                .findAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        CustomExceptions.formatMessage(Account.class, EntityNotFoundException.class, username)));
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(account.getRole().name());
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(account.getPassword())
                .authorities(grantedAuthority)
                .build();
    }

    @Override
    public AccountDto signUp(AccountDto accountDto) {

        if (accountRepository.existsById(accountDto.getUsername())) {
            throw CustomExceptions.raise(DuplicateEntityException.class, Account.class, accountDto.getUsername());
        }

        Account account = new Account()
                .setUsername(accountDto.getUsername())
                .setPassword(passwordEncoder.encode(accountDto.getPassword()))
                .setRole(Role.USER);

        User user = new User().setAccount(account);

        account.setUser(user);

        userRepository.save(user);
        accountRepository.save(account);

        return AccountMapper.toAccountDto(account);
    }
}
