package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoRepository {
    private final SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Todo> findAll() {
        return currentSession()
                .createQuery("FROM Todo", Todo.class)
                .list();
    }

    public List<Todo> findPaginated(int limit, int offset) {
        return currentSession()
                .createQuery("FROM Todo t ORDER BY t.id", Todo.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
    }

    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(currentSession().get(Todo.class, id));
    }

    public void save(Todo todo) {
        currentSession().persist(todo);
    }

    public void update(Todo todo) {
        currentSession().merge(todo);
    }

    public List<Todo> findCompleted(boolean completed) {
        return currentSession()
                .createQuery("FROM Todo t WHERE t.completed = :completed", Todo.class)
                .setParameter("completed", completed)
                .list();
    }

    public List<Todo> findPending() {
        return currentSession()
                .createQuery("FROM Todo t WHERE t.completed = false", Todo.class)
                .list();
    }

    public void delete(Todo todo) {
        currentSession().remove(todo);
    }


    public int countCompleted() {
        Long count = currentSession()
                .createQuery("SELECT COUNT(t) FROM Todo t WHERE t.completed = true", Long.class)
                .uniqueResult();
        return count != null ? count.intValue() : 0;
    }

    public int countPending() {
        Long count = currentSession()
                .createQuery("SELECT COUNT(t) FROM Todo t WHERE t.completed = false", Long.class)
                .uniqueResult();
        return count != null ? count.intValue() : 0;
    }
}
