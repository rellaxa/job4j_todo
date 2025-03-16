package ru.job4j.todo.store;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskStore {

    Task save(Task task);

    boolean update(Task task);

    boolean switchStatusByUser(Long id, Long userId, boolean done);

    boolean deleteByIdAndUser(Long id, Long userId);

    Optional<Task> findById(Long id);

    Collection<Task> findAll();

    Collection<Task> findTasksByStatus(boolean done);
}
