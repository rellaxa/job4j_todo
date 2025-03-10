package ru.job4j.todo.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
public class TaskDto {

    private Long id;

    private String title;

    private LocalDateTime created;

    private boolean done;

    private String userName;
}