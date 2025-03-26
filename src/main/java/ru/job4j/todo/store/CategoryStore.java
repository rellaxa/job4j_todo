package ru.job4j.todo.store;

import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface CategoryStore {

	Collection<Category> findAll();

	Collection<Category> findCategoriesById(Set<Integer> categoriesId);

	Optional<Category> findById(Long id);
}
