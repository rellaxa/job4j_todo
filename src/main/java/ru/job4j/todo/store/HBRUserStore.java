package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.utils.CrudRepository;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HBRUserStore implements UserStore {

	private final CrudRepository crudRepository;

	@Override
	public Optional<User> save(User user) {
		try {
			crudRepository.run(session -> session.persist(user));
			return Optional.of(user);
		} catch (Exception e) {
			return Optional.empty();
		}
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