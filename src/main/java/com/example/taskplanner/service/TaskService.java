package com.example.taskplanner.service;

import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.Priority;
import com.example.taskplanner.model.TaskUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getAll(String sortBy);
    List<Task> searchByTitle(String title);
    Optional<Task> getById(Long id);
    Task save(Task task);
    void delete(Long id);
    void markDone(Long id);

    Page<Task> getTasksWithPagination(Pageable pageable);
    List<Task> getTasksByPriority(Priority priority);
    List<Task> getTasksByStatus(boolean done);
    List<Task> getTasksByDueDateRange(LocalDate start, LocalDate end);
    Task partialUpdate(Long id, TaskUpdateDto updateDto);
}
