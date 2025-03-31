package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.dto.mapper.TaskMapper;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.TaskStore;

import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

	private final TaskStore taskStore;

	private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

	private final CategoryService categoryService;

	@Override
	public Task save(Task task, User user, Set<Integer> categoriesId) {
		updateCategories(task, categoriesId);
		task.setUser(user);
		return taskStore.save(task);
	}

	@Override
	public boolean update(Task task, Set<Integer> categoriesId) {
		updateCategories(task, categoriesId);
		return taskStore.update(task);
	}

	private void updateCategories(Task task, Set<Integer> categoriesId) {
		var categories = categoryService.findCategoriesById(categoriesId);
		task.setCategories(Set.copyOf(categories));
	}

	@Override
	public boolean switchStatusByUser(Long id, User user, boolean status) {
		return taskStore.switchStatusByUser(id, user.getId(), status);
	}

	@Override
	public boolean deleteByIdAndUser(Long id, User user) {
		return taskStore.deleteByIdAndUser(id, user.getId());
	}

	@Override
	public Optional<Task> findById(Long id) {
		return taskStore.findById(id);
	}

	@Override
	public Collection<TaskDto> getAll(User user) {
		return taskStore.findAll().stream()
				.map(task -> createTaskDto(task, user))
				.toList();
	}

	@Override
	public Collection<TaskDto> getCompletedTasks(User user) {
		return tasksByStatus(true, user);
	}

	@Override
	public Collection<TaskDto> getNewTasks(User user) {
		return tasksByStatus(false, user);
	}

	@Override
	public void setTimeZoneByUser(Task task, User user) {
		var time = task.getCreated()
				.atZone(ZoneId.of("UTC"))
				.withZoneSameInstant(ZoneId.of(user.getTimezone()))
				.toLocalDateTime();
		task.setCreated(time);
	}

	private Collection<TaskDto> tasksByStatus(boolean status, User user) {
		return taskStore.findTasksByStatus(status).stream()
				.map(task -> createTaskDto(task, user))
				.toList();
	}

	private TaskDto createTaskDto(Task task, User user) {
		setTimeZoneByUser(task, user);
		return taskMapper.getDtoFromEntity(task);
	}
}