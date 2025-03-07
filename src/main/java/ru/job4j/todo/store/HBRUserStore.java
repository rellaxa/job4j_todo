package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HBRUserStore implements UserStore {

	private final CrudRepository crudRepository;

	@Override
	public Optional<User> save(User user) {
		Optional<User> savedUser = Optional.of(user);
		try {
			crudRepository.run(session -> session.persist(user));
		} catch (Exception e) {
			savedUser = Optional.empty();
		}
		return savedUser;
	}

	@Override
	public Optional<User> findByLoginAndPassword(String login, String password) {
		return crudRepository.optional(
				"from User where login = :fLogin and password = :fPassword", User.class,
				Map.of(
						"fLogin", login,
						"fPassword", password
				)
		);
	}
}