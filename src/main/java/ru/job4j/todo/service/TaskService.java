package ru.job4j.todo.service;

import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {

    Task save(Task task, User user);

    boolean update(Task task);

    boolean deleteById(Long id);

    Optional<Task> findById(Long id);

    Collection<TaskDto> getAll();

    Collection<TaskDto> getCompletedTasks();

    Collection<TaskDto> getNewTasks();
}
