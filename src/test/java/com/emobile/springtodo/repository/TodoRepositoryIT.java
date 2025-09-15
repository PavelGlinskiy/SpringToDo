package com.emobile.springtodo.repository;

import com.emobile.springtodo.BasePostgresTest;
import com.emobile.springtodo.entity.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TodoRepositoryIT extends BasePostgresTest {

    @Autowired
    private TodoRepository repository;

    @Test
    @DisplayName("findAll возвращает все записи")
    @Sql({"classpath:db/testdata/clear.sql", "classpath:db/testdata/todos.sql"})
    void findAll_returnsAll() {
        List<Todo> todos = repository.findAll();
        assertThat(todos).hasSize(3);
    }

    @Test
    @DisplayName("findById находит существующую запись")
    @Sql({"classpath:db/testdata/clear.sql", "classpath:db/testdata/todos.sql"})
    void findById_found() {
        Optional<Todo> todo = repository.findById(100L);
        assertThat(todo).isPresent();
        assertThat(todo.get().getTitle()).isEqualTo("Task A");
        assertThat(todo.get().isCompleted()).isFalse();
    }

    @Test
    @DisplayName("save создаёт запись и заполняет id")
    @Sql({"classpath:db/testdata/clear.sql"})
    void save_creates() {
        Todo saved = repository.save(new Todo("New", false));
        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("update изменяет поля записи")
    @Sql({"classpath:db/testdata/clear.sql", "classpath:db/testdata/todos.sql"})
    void update_updates() {
        Todo toUpdate = new Todo(100L, "Updated", true);
        int rows = repository.update(100L, toUpdate);
        assertThat(rows).isEqualTo(1);
        Todo updated = repository.findById(100L).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("Updated");
        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("findCompleted возвращает только выполненные")
    @Sql({"classpath:db/testdata/clear.sql", "classpath:db/testdata/todos.sql"})
    void findCompleted_onlyCompleted() {
        List<Todo> completed = repository.findCompleted();
        assertThat(completed).extracting(Todo::isCompleted).containsOnly(true);
    }

    @Test
    @DisplayName("findPending возвращает только невыполненные")
    @Sql({"classpath:db/testdata/clear.sql", "classpath:db/testdata/todos.sql"})
    void findPending_onlyPending() {
        List<Todo> pending = repository.findPending();
        assertThat(pending).extracting(Todo::isCompleted).containsOnly(false);
    }

    @Test
    @DisplayName("delete удаляет запись по id")
    @Sql({"classpath:db/testdata/clear.sql", "classpath:db/testdata/todos.sql"})
    void delete_removes() {
        int rows = repository.delete(100L);
        assertThat(rows).isEqualTo(1);
        assertThat(repository.findById(100L)).isEmpty();
    }
}




