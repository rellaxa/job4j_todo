package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.dto.mapper.TaskMapper;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.TaskStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

	private final TaskStore taskStore;

	private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

	@Override
	public Task save(Task task, User user) {
		task.setUser(user);
		return taskStore.save(task);
	}

	@Override
	public boolean update(Task task) {
		return taskStore.update(task);
	}

	@Override
	public boolean deleteById(Long id) {
		return taskStore.deleteById(id);
	}

	@Override
	public Optional<Task> findById(Long id) {
		return taskStore.findById(id);
	}

	@Override
	public Collection<TaskDto> getAll() {
		var dtoTasks = new ArrayList<TaskDto>();
		for (var task : taskStore.findAll()) {
			dtoTasks.add(createTaskDto(task));
		}
		return dtoTasks;
	}

	@Override
	public Collection<TaskDto> getCompletedTasks() {
		var completedTasks = new ArrayList<TaskDto>();
		taskStore.findCompletedTasks().forEach(task -> completedTasks.add(createTaskDto(task)));
		return completedTasks;
	}

	@Override
	public Collection<TaskDto> getNewTasks() {
		var newTasks = new ArrayList<TaskDto>();
		taskStore.findNewTasks().forEach(task -> newTasks.add(createTaskDto(task)));
		return newTasks;
	}

	private TaskDto createTaskDto(Task task) {
		return taskMapper.getDtoFromEntity(task);
	}
}