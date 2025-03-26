package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.utils.CrudRepository;

import java.util.*;

@Repository
@AllArgsConstructor

public class HBRCategoryStore implements CategoryStore {

	private final CrudRepository crudRepository;

	@Override
	public Collection<Category> findAll() {
		return crudRepository.query("from Category order by id ASC", Category.class);
	}

	@Override
	public Collection<Category> findCategoriesById(Set<Integer> categoriesId) {
		return crudRepository.query("from Category WHERE id IN :fId order by id ASC", Category.class,
				Map.of("fId", categoriesId));
	}

	@Override
	public Optional<Category> findById(Long id) {
		return crudRepository.optional("from Category where id = :fId", Category.class,
				Map.of("fId", id)
		);
	}
}
