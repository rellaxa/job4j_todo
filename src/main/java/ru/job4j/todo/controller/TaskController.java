package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.Collection;

@Controller
@RequestMapping({"/", "/tasks",})
@AllArgsConstructor
public class TaskController {

	private final TaskService taskService;

	@GetMapping
	public String getAllTasks(Model model) {
		Collection<TaskDto> allTasks = taskService.getAll();
		model.addAttribute("tasks", allTasks);
		return "/tasks/allTasks";
	}

	@GetMapping("/completed")
	public String getCompletedTasks(Model model) {
		model.addAttribute("tasks", taskService.getCompletedTasks());
		return "/tasks/completedTasks";
	}

	@GetMapping("/new")
	public String getNewTasks(Model model) {
		model.addAttribute("tasks", taskService.getNewTasks());
		return "/tasks/newTasks";
	}

	@GetMapping("/create")
	public String getCreationPage() {
		return "/tasks/create";
	}

	@PostMapping("/create")
	public String createTask(@ModelAttribute Task task, Model model) {
		try {
			taskService.save(task);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "/errors/404";
		}
		return "redirect:/tasks";
	}

	@GetMapping("{id}")
	public String getTask(@PathVariable Long id, Model model) {
		var taskOptional = taskService.findById(id);
		if (taskOptional.isEmpty()) {
			model.addAttribute("error", "Task with id " + id + " not found.");
			return "/errors/404";
		}
		model.addAttribute("task", taskOptional.get());
		return "/tasks/one";
	}

	@GetMapping("/update/{id}")
	public String getUpdateTaskPage(@PathVariable Long id, Model model) {
		var taskOptional = taskService.findById(id);
		if (taskOptional.isEmpty()) {
			model.addAttribute("error", "Task with id " + id + " not found.");
			return "/errors/404";
		}
		model.addAttribute("task", taskOptional.get());
		return "/tasks/update";
	}

	@PostMapping("/update")
	public String updateTask(@ModelAttribute Task task, Model model) {
		try {
			var isUpdated = taskService.update(task);
			if (!isUpdated) {
				model.addAttribute("error", "Task with id " + task.getId() + " not found.");
				return "/errors/404";
			}
			return "redirect:/tasks";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "/errors/404";
		}
	}

	@GetMapping("/delete/{id}")
	public String deleteTask(@PathVariable Long id, Model model) {
		var isDeleted = taskService.deleteById(id);
		if (!isDeleted) {
			model.addAttribute("error", "Task with id " + id + " not found.");
			return "/errors/404";
		}
		return "redirect:/tasks";
	}

	@GetMapping("/switchStatus/{id}")
	public String switchStatus(@PathVariable Long id, Model model) {
		var taskOptional = taskService.findById(id);
		if (taskOptional.isEmpty()) {
			model.addAttribute("error", "Task with id " + id + " not found.");
			return "/errors/404";
		}
		var task = taskOptional.get();
		task.setDone(!task.isDone());
		taskService.update(task);
		return "redirect:/tasks";
	}
}
