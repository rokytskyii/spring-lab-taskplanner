package com.example.taskplanner.repository;

import com.example.taskplanner.model.Priority;
import com.example.taskplanner.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    Optional<Task> findById(Long id);
    Task save(Task task);
    void deleteById(Long id);
    List<Task> findByPriority(Priority priority);
    List<Task> findByStatus(boolean done);
}
