package li.yuhang.fogofworld.server.repository;

import li.yuhang.fogofworld.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
