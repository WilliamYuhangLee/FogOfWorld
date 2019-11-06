package li.yuhang.fogofworld.server.service;

import li.yuhang.fogofworld.server.dto.AccountDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {

    AccountDto signUp(AccountDto accountDto);

}
