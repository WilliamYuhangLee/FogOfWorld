package li.yuhang.fogofworld.server.dto;

import li.yuhang.fogofworld.server.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public static AccountDto toAccountDto(Account account) {
        return new AccountDto()
        .setUsername(account.getUsername())
        .setPassword(account.getPassword())
        .setUser_id(account.getUser().getId())
        .setRole(account.getRole().name());
    }
}
