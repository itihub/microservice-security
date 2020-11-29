package xyz.itihub.security.user;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends JpaSpecificationExecutor<User>, CrudRepository<User, Long> {


    User findByUsername(String username);

    List<User> findByName(String name);

    List<User> findByNameLike(String name);

}
