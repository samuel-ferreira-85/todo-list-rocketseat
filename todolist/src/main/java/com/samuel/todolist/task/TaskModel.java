package com.samuel.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_task")
public class TaskModel {
    
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    private LocalDateTime createdAt;

    private UUID userId;
}
