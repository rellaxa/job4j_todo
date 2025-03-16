package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.store.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HBRPriorityStore implements PriorityStore {

	private final CrudRepository crudRepository;

	@Override
	public Collection<Priority> findAll() {
		return crudRepository.query("from Priority order by id ASC", Priority.class);
	}

	@Override
	public Optional<Priority> findById(int id) {
		return crudRepository.optional("from Priority where id = :fId", Priority.class,
				Map.of("fId", id));
	}
}
