package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class HBRUserStore implements UserStore {

	private final SessionFactory sf;

	@Override
	public Optional<User> save(User user) {
		try (Session session = sf.openSession()) {
			var transaction = session.beginTransaction();
			try {
				session.save(user);
				transaction.commit();
				return Optional.of(user);
			} catch (Exception e) {
				transaction.rollback();
				return Optional.empty();
			}
		}
	}

	@Override
	public Optional<User> findByLoginAndPassword(String login, String password) {
		try (Session session = sf.openSession()) {
			var transaction = session.beginTransaction();
			try {
				var optionalUser = session.createQuery("from User where login = :fLogin and password = :fPassword", User.class)
						.setParameter("fLogin", login)
						.setParameter("fPassword", password)
						.uniqueResultOptional();
				transaction.commit();
				return optionalUser;
			} catch (Exception e) {
				transaction.rollback();
				return Optional.empty();
			}
		}
	}
}