package com.samuel.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samuel.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        
        var userId = request.getAttribute("userId");
        taskModel.setUserId((UUID) userId);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("A data de início / data de término deve ser maior que a data atual");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("A data de início de início deve menor que a data de término");
        }

        var taskSaved = taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(taskSaved);
    }

    @GetMapping
    public List<TaskModel> list(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        return taskRepository.findByUserId((UUID) userId);        
    }

    @PutMapping("/{id}")
    public TaskModel update( @RequestBody TaskModel task, @PathVariable UUID id, HttpServletRequest request ) {
        var userId = request.getAttribute("userId");
        task.setUserId((UUID) userId);
        
        task.setId(id); 
        return taskRepository.save(task);
    }

    @PatchMapping("/{id}")
    public TaskModel updatePatch( @RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request ) {
        var task = taskRepository.findById(id);

        Utils.copyNonNullProperties(taskModel, task.get());

        return taskRepository.save(task.get());
    }
}
