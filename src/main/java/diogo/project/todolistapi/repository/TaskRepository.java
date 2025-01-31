package diogo.project.todolistapi.repository;

import diogo.project.todolistapi.domain.tasks.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
