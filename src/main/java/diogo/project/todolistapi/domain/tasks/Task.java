package diogo.project.todolistapi.domain.tasks;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "Tasks")
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer taskId;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @NotBlank
    @Column(name = "taskName", nullable = false)
    private String taskName;

    public Integer getId() {
        return taskId;
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
