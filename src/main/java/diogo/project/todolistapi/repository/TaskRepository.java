package diogo.project.todolistapi.repository;

import diogo.project.todolistapi.domain.tasks.Task;
import diogo.project.todolistapi.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUser(User user, Pageable pageable);
    Optional<Task> findByIdAndUser(Long id, User user);

}
