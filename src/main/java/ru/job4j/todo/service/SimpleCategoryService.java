package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.CategoryStore;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {

	private final CategoryStore categoryStore;

	@Override
	public Collection<Category> findAll() {
		return categoryStore.findAll();
	}

	@Override
	public Collection<Category> findCategoriesById(Set<Integer> categoriesId) {
		return categoryStore.findCategoriesById(categoriesId);
	}

	@Override
	public Optional<Category> findById(Long id) {
		return categoryStore.findById(id);
	}
}
