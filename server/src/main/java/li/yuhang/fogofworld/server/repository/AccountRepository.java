package li.yuhang.fogofworld.server.repository;

import li.yuhang.fogofworld.server.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findAccountByUsername(String username);

}
