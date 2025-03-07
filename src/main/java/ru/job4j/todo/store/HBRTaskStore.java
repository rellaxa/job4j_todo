package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.*;

@Repository
@AllArgsConstructor
public class HBRTaskStore implements TaskStore {

	private final CrudRepository crudRepository;

	@Override
	public Task save(Task task) {
		crudRepository.run(session -> session.persist(task));
		return task;
	}

	@Override
	public boolean update(Task task) {
		return crudRepository.run("""
								update Task set title = :fTitle, description = :fDescription, created = :fCreated, done = :fDone where id = :fId
								""",
						Map.of("fTitle", task.getTitle(),
								"fDescription", task.getDescription(),
								"fCreated", task.getCreated(),
								"fDone", task.isDone(),
								"fId", task.getId()
						)
		);
	}

	@Override
	public boolean deleteById(Long id) {
		return crudRepository.run("delete from Task where id = :fId",
				Map.of("fId", id));
	}

	@Override
	public Optional<Task> findById(Long id) {
		return crudRepository.optional("from Task where id = :fId", Task.class,
				Map.of("fId", id));
	}

	@Override
	public Collection<Task> findAll() {
		return crudRepository.query("from Task order by id", Task.class);
	}

	@Override
	public Collection<Task> findCompletedTasks() {
		return findTasksByStatus(true);
	}

	@Override
	public Collection<Task> findNewTasks() {
		return findTasksByStatus(false);
	}

	private Collection<Task> findTasksByStatus(boolean status) {
		return crudRepository.query("from Task where done = :fDone order by id", Task.class,
				Map.of("fDone", status));
	}
}