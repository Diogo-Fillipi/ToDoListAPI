package diogo.project.todolistapi.controller;

import diogo.project.todolistapi.domain.tasks.PaginatedResponse;
import diogo.project.todolistapi.domain.tasks.Task;
import diogo.project.todolistapi.domain.user.User;
import diogo.project.todolistapi.repository.TaskRepository;
import diogo.project.todolistapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ToDoListController {

    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    public ToDoListController(final TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return (User)this.userRepository.findByEmail(username);
    }

    @GetMapping("/login")
    public ResponseEntity<User> login(){
        return null;
    }

    @PostMapping("/createTask")
    public ResponseEntity<Task> create(@RequestBody Task task){
        User user = getAuthenticatedUser();
        task.setUser(user);
        Task newTask = this.taskRepository.save(task);
        return new ResponseEntity<Task>(HttpStatusCode.valueOf(201));
    }

    @GetMapping("/readAll")
    public ResponseEntity<PaginatedResponse<Task>> readAll(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size) {
        User user = getAuthenticatedUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = this.taskRepository.findByUser(user, pageable);


        PaginatedResponse<Task> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setData(taskPage.getContent());
        paginatedResponse.setPage(taskPage.getNumber());
        paginatedResponse.setLimit(taskPage.getSize());
        paginatedResponse.setTotal(taskPage.getTotalElements());
        return ResponseEntity.ok(paginatedResponse);
    }

    @GetMapping("/readById")
    public ResponseEntity<Task> readById(@RequestParam Long id){
        User user = getAuthenticatedUser();
        Optional<Task> taskOptional = this.taskRepository.findByIdAndUser(id, user);
        if(taskOptional.isPresent()){
            Task taskById = taskOptional.get();
            return ResponseEntity.ok(taskById);
        }else{
            return ResponseEntity.noContent().build();
        }

    }

    @PutMapping("/updateById")
    public ResponseEntity<Task> updateById(@RequestBody Task task, @PathVariable (name = "id") Long id){
        User user = getAuthenticatedUser();
        Optional<Task> taskToUpdateOptional = this.taskRepository.findByIdAndUser(id, user);
        if(taskToUpdateOptional.isPresent()){
            Task taskToUpdate = taskToUpdateOptional.get();
            taskToUpdate.setTaskName(task.getTaskName());
            taskToUpdate.setDescription(task.getDescription());
            return ResponseEntity.ok(taskToUpdate);
        }else{
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<Task> deleteById(@RequestParam Long id){
        User user = getAuthenticatedUser();
        this.taskRepository.findByIdAndUser(id, user).ifPresent(this.taskRepository::delete);

        return ResponseEntity.noContent().build();
    }



}
