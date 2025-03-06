package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.UserStore;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {

	private final UserStore userStore;

	@Override
	public Optional<User> save(User user) {
		return userStore.save(user);
	}

	@Override
	public Optional<User> findByLoginAndPassword(String login, String password) {
		return userStore.findByLoginAndPassword(login, password);
	}
}
