package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import java.util.Set;

@Controller
@RequestMapping({"/", "/tasks",})
@AllArgsConstructor
@SessionAttributes("user")
public class TaskController {

	private final TaskService taskService;

	private final PriorityService priorityService;

	private final CategoryService categoryService;

	@GetMapping
	public String getAllTasks(Model model) {
		model.addAttribute("tasks", taskService.getAll());
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
	public String getCreationPage(Model model) {
		model.addAttribute("priorities", priorityService.findAll());
		model.addAttribute("categories", categoryService.findAll());
		return "/tasks/create";
	}

	@PostMapping("/create")
	public String create(@ModelAttribute Task task, @RequestParam(required = false) Set<Integer> categoriesId, @SessionAttribute User user, Model model) {
		if (categoriesId == null) {
			model.addAttribute("error", "Please select at least one category.");
			return "/errors/404";
		}
		taskService.save(task, user, categoriesId);
		return "redirect:/tasks";
	}

	@GetMapping("{id}")
	public String getTask(@PathVariable Long id, @SessionAttribute User user, Model model) {
		if (taskByUser(id, user, model)) {
			return "/errors/404";
		}
		return "/tasks/one";
	}

	@GetMapping("/update/{id}")
	public String getUpdateTaskPage(@PathVariable Long id, @SessionAttribute User user, Model model) {
		if (taskByUser(id, user, model)) {
			return "/errors/404";
		}
		model.addAttribute("priorities", priorityService.findAll());
		model.addAttribute("allCategories", categoryService.findAll());
		return "/tasks/update";
	}

	private boolean taskByUser(@PathVariable Long id, @SessionAttribute User user, Model model) {
		var taskOptional = taskService.findById(id);
		if (taskOptional.isEmpty()) {
			model.addAttribute("error", "Task with id " + id + " not found.");
			return true;
		}
		var task = taskOptional.get();
		if (!task.getUser().equals(user)) {
			model.addAttribute("error", "You do not have permission to edit this task.");
			return true;
		}
		model.addAttribute("task", task);
		return false;
	}

	@PostMapping("/update")
	public String updateTask(@ModelAttribute Task task, @RequestParam(required = false) Set<Integer> categoriesId, Model model) {
		if (categoriesId == null) {
			model.addAttribute("error", "At least one category is required.");
			return "/errors/404";
		}
		taskService.update(task, categoriesId);
		return "redirect:/tasks";
	}

	@GetMapping("/delete/{id}")
	public String deleteTask(@PathVariable Long id, @SessionAttribute User user, Model model) {
		var isDeleted = taskService.deleteByIdAndUser(id, user);
		if (!isDeleted) {
			model.addAttribute("error", "Task with id " + id + " not found or you have no permission to delete this task.");
			return "/errors/404";
		}
		return "redirect:/tasks";
	}

	@GetMapping("/switchStatus/{id}/{status}")
	public String switchStatus(@PathVariable Long id, @PathVariable boolean status,
							   @SessionAttribute User user, Model model) {
		var isSuccess = taskService.switchStatusByUser(id, user, status);
		if (!isSuccess) {
			model.addAttribute("error", "Task with id " + id + " not found or you have no permission to edit this task.");
			return "/errors/404";
		}
		return "redirect:/tasks";
	}
}