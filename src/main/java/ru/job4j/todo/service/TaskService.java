package ru.job4j.todo.service;

import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface TaskService {

    Task save(Task task, User user, Set<Integer> categoriesId);

    boolean update(Task task, Set<Integer> categoriesId);

    boolean switchStatusByUser(Long id, User user, boolean status);

    boolean deleteByIdAndUser(Long id, User user);

    Optional<Task> findById(Long id);

    Collection<TaskDto> getAll();

    Collection<TaskDto> getCompletedTasks();

    Collection<TaskDto> getNewTasks();
}
