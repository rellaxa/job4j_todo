package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.configuration.HibernateConfiguration;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HiberTaskStore implements TaskStore {

    private final SessionFactory sf;

    @Override
    public Task save(Task task) {
        try (var session = sf.getCurrentSession()) {
            var transaction = session.beginTransaction();
            try {
                session.save(task);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        var updated = false;
        try (var session = sf.getCurrentSession()) {
            var transaction = session.beginTransaction();
            try {
                updated = session.createQuery("""
                                update Task set title = :fTitle, description = :fDescription, created = :fCreated, done = :fDone where id = :fId
                                """)
                        .setParameter("fTitle", task.getTitle())
                        .setParameter("fDescription", task.getDescription())
                        .setParameter("fCreated", task.getCreated())
                        .setParameter("fDone", task.isDone())
                        .setParameter("fId", task.getId())
                        .executeUpdate() > 0;
            } catch (Exception e) {
                transaction.rollback();
            }
        }
        return updated;
    }

    @Override
    public boolean deleteById(Long id) {
        var deleted = false;
        try (var session = sf.getCurrentSession()) {
            var transaction = session.beginTransaction();
            try {
                deleted = session.createQuery("delete from Task where id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate() > 0;
            } catch (Exception e) {
                transaction.rollback();
            }
        }
        return deleted;
    }

    @Override
    public Optional<Task> findById(Long id) {
		Optional<Task> task = Optional.empty();
        try (var session = sf.getCurrentSession()) {
            var transaction = session.beginTransaction();
            try {
                task = session.createQuery("from Task where id = :fId", Task.class)
                        .setParameter("fId", id)
                        .uniqueResultOptional();
				transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
        return task;
    }

    @Override
    public Collection<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try (var session = sf.getCurrentSession()) {
            var transaction = session.beginTransaction();
            try {
                tasks = session.createQuery("from Task order by id", Task.class).list();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
        return tasks;
    }

    @Override
    public Collection<Task> findCompletedTasks() {
        return findTasksByStatus(true);
    }

    @Override
    public Collection<Task> findNewTasks() {
        return findTasksByStatus(false);
    }

    private Collection<Task> findTasksByStatus(boolean status) {
        List<Task> newTasks = new ArrayList<>();
        try (var session = sf.getCurrentSession()) {
            var transaction = session.beginTransaction();
            try {
                newTasks = session.createQuery("from Task where done = :fDone order by id", Task.class)
                        .setParameter("fDone", status)
                        .list();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
        return newTasks;
    }
}