package ru.job4j.todo.service;

import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface CategoryService {

	Collection<Category> findAll();

	Optional<Category> findById(Long id);

	Collection<Category> findCategoriesById(Set<Integer> categoriesId);
}
