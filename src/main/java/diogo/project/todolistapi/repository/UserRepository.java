package diogo.project.todolistapi.repository;

import diogo.project.todolistapi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
     UserDetails findByEmail(String email);
}
