package li.yuhang.fogofworld.server.repository;

import li.yuhang.fogofworld.server.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

    Account findAccountByUsername(String username);

}
