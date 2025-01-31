package diogo.project.todolistapi.controller;

import diogo.project.todolistapi.domain.tasks.PaginatedResponse;
import diogo.project.todolistapi.domain.tasks.Task;
import diogo.project.todolistapi.domain.user.User;
import diogo.project.todolistapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ToDoListController {

    @Autowired
    private final TaskRepository taskRepository;

    public ToDoListController(final TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }



    @GetMapping("/login")
    public ResponseEntity<User> login(){
        return null;
    }

    @PostMapping("/createTask")
    public ResponseEntity<Task> create(@RequestBody Task task){
        Task newTask = this.taskRepository.save(task);
        return new ResponseEntity<Task>(HttpStatusCode.valueOf(201));
    }

    @GetMapping("/readAll")
    public ResponseEntity<PaginatedResponse<Task>> readAll(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = this.taskRepository.findAll(pageable);


        PaginatedResponse<Task> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setData(taskPage.getContent());
        paginatedResponse.setPage(taskPage.getNumber());
        paginatedResponse.setLimit(taskPage.getSize());
        paginatedResponse.setTotal(taskPage.getTotalElements());
        return ResponseEntity.ok(paginatedResponse);
    }

    @GetMapping("/readById")
    public ResponseEntity<Task> readById(@RequestParam Long id){
        Optional<Task> taskOptional = this.taskRepository.findById(id);
        if(taskOptional.isPresent()){
            Task taskById = taskOptional.get();
            return ResponseEntity.ok(taskById);
        }else{
            return ResponseEntity.noContent().build();
        }

    }

    @PutMapping("/updateById")
    public ResponseEntity<Task> updateById(@RequestBody Task task, @PathVariable (name = "id") Long id){
        Optional<Task> taskToUpdateOptional = this.taskRepository.findById(id);
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
        this.taskRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }



}
