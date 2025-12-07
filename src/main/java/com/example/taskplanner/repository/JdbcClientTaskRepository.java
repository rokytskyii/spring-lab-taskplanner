package com.example.taskplanner.repository;

import com.example.taskplanner.model.Priority;
import com.example.taskplanner.model.Task;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcClientTaskRepository implements TaskRepository {

    private final JdbcClient jdbcClient;

    public JdbcClientTaskRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Task> findAll() {
        return jdbcClient.sql("SELECT * FROM tasks").query(new TaskRowMapper()).list();
    }

    @Override
    public Optional<Task> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM tasks WHERE id = :id")
                .param("id", id)
                .query(new TaskRowMapper()).optional();
    }

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcClient.sql("INSERT INTO tasks (title, description, due_date, priority, done) VALUES (:title, :description, :dueDate, :priority, :done)")
                    .param("title", task.getTitle())
                    .param("description", task.getDescription())
                    .param("dueDate", task.getDueDate())
                    .param("priority", task.getPriority() != null ? task.getPriority().name() : null)
                    .param("done", task.isDone())
                    .update(keyHolder, "id");
            if (keyHolder.getKeys() != null) {
                task.setId(((Number) keyHolder.getKeys().get("id")).longValue());
            }
        } else {
            jdbcClient.sql("UPDATE tasks SET title=:title, description=:description, due_date=:dueDate, priority=:priority, done=:done WHERE id=:id")
                    .param("title", task.getTitle())
                    .param("description", task.getDescription())
                    .param("dueDate", task.getDueDate())
                    .param("priority", task.getPriority() != null ? task.getPriority().name() : null)
                    .param("done", task.isDone())
                    .param("id", task.getId())
                    .update();
        }
        return task;
    }

    @Override
    public void deleteById(Long id) {
        jdbcClient.sql("DELETE FROM tasks WHERE id = :id").param("id", id).update();
    }

    @Override
    public List<Task> findByPriority(Priority priority) {
        return jdbcClient.sql("SELECT * FROM tasks WHERE priority = :priority")
                .param("priority", priority.name())
                .query(new TaskRowMapper())
                .list();
    }

    @Override
    public List<Task> findByStatus(boolean done) {
        return jdbcClient.sql("SELECT * FROM tasks WHERE done = :done")
                .param("done", done)
                .query(new TaskRowMapper())
                .list();
    }
}