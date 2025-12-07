package com.example.taskplanner.repository;

import com.example.taskplanner.model.Priority;
import com.example.taskplanner.model.Task;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class JdbcTemplateTaskRepository implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Task> findAll() {
        return jdbcTemplate.query("SELECT * FROM tasks", new TaskRowMapper());
    }

    @Override
    public Optional<Task> findById(Long id) {
        List<Task> tasks = jdbcTemplate.query("SELECT * FROM tasks WHERE id = ?", new TaskRowMapper(), id);
        return tasks.stream().findFirst();
    }

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            String sql = "INSERT INTO tasks (title, description, due_date, priority, done) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, task.getTitle());
                ps.setString(2, task.getDescription());
                ps.setObject(3, task.getDueDate());
                ps.setString(4, task.getPriority() != null ? task.getPriority().name() : null);
                ps.setBoolean(5, task.isDone());
                return ps;
            }, keyHolder);

            if (keyHolder.getKeys() != null) {
                task.setId(((Number) keyHolder.getKeys().get("id")).longValue());
            }
        } else {
            String sql = "UPDATE tasks SET title=?, description=?, due_date=?, priority=?, done=? WHERE id=?";
            jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), task.getDueDate(),
                    task.getPriority() != null ? task.getPriority().name() : null,
                    task.isDone(), task.getId());
        }
        return task;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
    }

    @Override
    public List<Task> findByPriority(Priority priority) {
        String sql = "SELECT * FROM tasks WHERE priority = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), priority.name());
    }

    @Override
    public List<Task> findByStatus(boolean done) {
        String sql = "SELECT * FROM tasks WHERE done = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), done);
    }
}