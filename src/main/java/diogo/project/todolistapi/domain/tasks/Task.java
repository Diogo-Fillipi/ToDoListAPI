package diogo.project.todolistapi.domain.tasks;

import diogo.project.todolistapi.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "Tasks")
public class Task {

    public Task(String description, String taskName) {
        this.description = description;
        this.taskName = taskName;
    }

    public Task() {}


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer taskId;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @NotBlank
    @Column(name = "taskName", nullable = false)
    private String taskName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Integer getId() {
        return taskId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Integer id) {
        this.taskId = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
