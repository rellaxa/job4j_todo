package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.utils.CrudRepository;

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
		return crudRepository.tx(session -> session.merge(task)) != null;
	}

	@Override
	public boolean switchStatusByUser(Long id, Long userId, boolean done) {
		return crudRepository.run("update Task set done = :fDone where id = :fId and user_id = :fUserId",
				Map.of("fDone", done,
						"fId", id,
						"fUserId", userId
				)
		);
	}

	@Override
	public boolean deleteByIdAndUser(Long id, Long userId) {
		return crudRepository.run("delete from Task where id = :fId and user_id = :fUserId",
				Map.of("fId", id,
						"fUserId", userId
				)
		);
	}

	@Override
	public Optional<Task> findById(Long id) {
		return crudRepository.optional("from Task t JOIN FETCH t.priority JOIN FETCH t.categories where t.id = :fId", Task.class,
				Map.of("fId", id));
	}

	@Override
	public Collection<Task> findAll() {
		return crudRepository.query("from Task t JOIN FETCH t.priority order by t.id ASC", Task.class);
	}

	@Override
	public Collection<Task> findTasksByStatus(boolean status) {
		return crudRepository.query("from Task t JOIN FETCH t.priority where t.done = :fDone order by t.id ASC", Task.class,
				Map.of("fDone", status));
	}

}