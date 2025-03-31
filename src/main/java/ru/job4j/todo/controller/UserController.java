package ru.job4j.todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.controller.utils.TimeZoneStorage;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

@Controller
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    private final TimeZoneStorage timeZoneStorage;

    public UserController(UserService userService, TimeZoneStorage timeZoneStorage) {
        this.userService = userService;
        this.timeZoneStorage = timeZoneStorage;
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        var zones = timeZoneStorage.getTimeZones();
        model.addAttribute("timeZones", zones);
        model.addAttribute("defaultTimeZone", timeZoneStorage.getDefaultTimeZone().getID());
        return "/users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("newUser") User user, Model model, HttpServletRequest request) {
        var savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            var error = String.format("User with login: %s already exists.", user.getLogin());
            model.addAttribute("error", error);
            return "errors/404";
        }
        initSession(savedUser.get(), request);
        return "redirect:/tasks";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOptional = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "The login or password incorrect.");
            return "errors/404";
        }
        var loginedUser = userOptional.get();
        if (loginedUser.getTimezone() == null) {
            loginedUser.setTimezone(timeZoneStorage.getDefaultTimeZone().getID());
        }
        initSession(loginedUser, request);
        return "redirect:/tasks";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    private void initSession(User user, HttpServletRequest request) {
        var session = request.getSession();
        session.setAttribute("user", user);
    }
}
