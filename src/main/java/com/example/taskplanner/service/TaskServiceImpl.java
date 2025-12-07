package com.example.taskplanner.service;

import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.Priority;
import com.example.taskplanner.model.TaskUpdateDto;
import com.example.taskplanner.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private NotificationService notificationService;

    @Autowired
    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public List<Task> getAll(String sortBy) {
        List<Task> tasks = repository.findAll();

        if ("date".equalsIgnoreCase(sortBy)) {
            tasks.sort(Comparator.comparing(t ->
                    t.getDueDate() != null ? t.getDueDate() : java.time.LocalDate.MAX
            ));
        } else if ("priority".equalsIgnoreCase(sortBy)) {
            tasks.sort((t1, t2) -> Integer.compare(priorityScore(t2), priorityScore(t1)));
        }

        return tasks;
    }

    private int priorityScore(Task t) {
        if (t.getPriority() == null) return 0;
        switch (t.getPriority()) {
            case HIGH:
                return 3;
            case MEDIUM:
                return 2;
            case LOW:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public Optional<Task> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Task save(Task task) {
        Task saved = repository.save(task);
        if (notificationService != null) {
            notificationService.notify("Task saved: " + saved.getTitle());
        }
        return saved;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void markDone(Long id) {
        repository.findById(id).ifPresent(t -> {
            t.setDone(true);
            repository.save(t);
        });
    }

    @Override
    public Page<Task> getTasksWithPagination(Pageable pageable) {
        List<Task> allTasks = repository.findAll();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allTasks.size());

        return new org.springframework.data.domain.PageImpl<>(
                allTasks.subList(start, end),
                pageable,
                allTasks.size()
        );
    }

    @Override
    public List<Task> getTasksByPriority(Priority priority) {
        return repository.findAll().stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasksByStatus(boolean done) {
        return repository.findAll().stream()
                .filter(task -> task.isDone() == done)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasksByDueDateRange(LocalDate start, LocalDate end) {
        return repository.findAll().stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> !task.getDueDate().isBefore(start) && !task.getDueDate().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public Task partialUpdate(Long id, TaskUpdateDto updateDto) {
        Optional<Task> existingTaskOpt = repository.findAll().stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();

        if (!existingTaskOpt.isPresent()) {
            throw new RuntimeException("Task not found with id: " + id);
        }

        Task existingTask = existingTaskOpt.get();

        if (updateDto.getTitle() != null) {
            existingTask.setTitle(updateDto.getTitle());
        }
        if (updateDto.getDescription() != null) {
            existingTask.setDescription(updateDto.getDescription());
        }
        if (updateDto.getDueDate() != null) {
            existingTask.setDueDate(updateDto.getDueDate());
        }
        if (updateDto.getPriority() != null) {
            existingTask.setPriority(updateDto.getPriority());
        }
        if (updateDto.getDone() != null) {
            existingTask.setDone(updateDto.getDone());
        }

        return repository.save(existingTask);
    }
}
