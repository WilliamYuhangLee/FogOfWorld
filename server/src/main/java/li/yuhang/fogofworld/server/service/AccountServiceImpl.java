package li.yuhang.fogofworld.server.service;

import li.yuhang.fogofworld.server.dto.AccountDto;
import li.yuhang.fogofworld.server.exception.ApiExceptionFactory;
import li.yuhang.fogofworld.server.exception.DuplicateEntityException;
import li.yuhang.fogofworld.server.model.Account;
import li.yuhang.fogofworld.server.dto.AccountMapper;
import li.yuhang.fogofworld.server.model.User;
import li.yuhang.fogofworld.server.repository.AccountRepository;
import li.yuhang.fogofworld.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AccountDto signUp(AccountDto accountDto) {

        if (accountRepository.existsById(accountDto.getUsername())) {
            throw ApiExceptionFactory.exception(Account.class, DuplicateEntityException.class, accountDto.getUsername());
        }

        Account account = new Account()
        .setUsername(accountDto.getUsername())
        .setPassword(accountDto.getPassword());

        User user = new User().setAccount(account);

        account.setUser(user);

        userRepository.save(user);
        accountRepository.save(account);

        return AccountMapper.toAccountDto(account);
    }
}
