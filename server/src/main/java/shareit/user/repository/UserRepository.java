package shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shareit.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
