package com.example.taskplanner.repository;

import com.example.taskplanner.model.Priority;
import com.example.taskplanner.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));

        java.sql.Date date = rs.getDate("due_date");
        if (date != null) {
            task.setDueDate(date.toLocalDate());
        }

        String priorityStr = rs.getString("priority");
        if (priorityStr != null) {
            task.setPriority(Priority.valueOf(priorityStr));
        }
        task.setDone(rs.getBoolean("done"));
        return task;
    }
}