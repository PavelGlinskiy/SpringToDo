package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Todo> todoRowMapper = (rs, rowNum) ->
            new Todo(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getBoolean("completed")
            );

    public List<Todo> findAll() {
        return jdbcTemplate.query("SELECT * FROM todo", todoRowMapper);
    }

    public List<Todo> findPaginated(int limit, int offset) {
        return jdbcTemplate.query("SELECT * FROM todo ORDER BY id LIMIT ? OFFSET ?", todoRowMapper,
                limit,
                offset);
    }



    public Optional<Todo> findById(Long id) {
        List<Todo> results = jdbcTemplate.query(
                "SELECT * FROM todo WHERE id = ?",
                todoRowMapper,
                id
        );
        return results.stream().findFirst();
    }

    public Todo save(Todo todo) {
        String sql = "INSERT INTO todo (title, completed) VALUES (?, ?) RETURNING id";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, todo.getTitle());
            ps.setBoolean(2, todo.isCompleted());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        todo.setId(id);

        return todo;
    }

    public int update(Long id, Todo todo) {
        return jdbcTemplate.update(
                "UPDATE todo SET title = ?, completed = ? WHERE id = ?",
                todo.getTitle(),
                todo.isCompleted(),
                id
        );
    }

    public List<Todo> findCompleted() {
        return jdbcTemplate.query("SELECT * FROM todo WHERE completed = ?", todoRowMapper, true);
    }

    public List<Todo> findPending() {
        return jdbcTemplate.query("SELECT * FROM todo WHERE completed = ?", todoRowMapper, false);
    }

    public int delete(Long id) {
        return jdbcTemplate.update("DELETE FROM todo WHERE id = ?", id);
    }

    public int countCompleted() {
        String sql = "SELECT COUNT(*) FROM todo WHERE completed = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, true);
        return count != null ? count : 0;
    }

    public int countPending() {
        String sql = "SELECT COUNT(*) FROM todo WHERE completed = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, false);
        return count != null ? count : 0;
    }
}
