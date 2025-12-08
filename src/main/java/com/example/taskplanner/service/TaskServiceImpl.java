package com.example.taskplanner.service;

import com.example.taskplanner.model.Category;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.Priority;
import com.example.taskplanner.model.TaskUpdateDto;
import com.example.taskplanner.repository.CategoryRepository;
import com.example.taskplanner.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Task> getAll(String sortBy) {
        if ("priority".equalsIgnoreCase(sortBy)) {
            return taskRepository.findAll(Sort.by(Sort.Direction.DESC, "priority"));
        } else if ("date".equalsIgnoreCase(sortBy)) {
            return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "dueDate"));
        }
        return taskRepository.findAll();
    }

    @Override
    public List<Task> searchByTitle(String title) {
        return taskRepository.searchByTitle(title);
    }

    @Override
    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task save(Task task) {
        if (task.getCategory() != null) {
            if (task.getCategory().getId() != null) {
                Category existingCat = categoryRepository.findById(task.getCategory().getId())
                        .orElse(null);
                task.setCategory(existingCat);
            } else if (task.getCategory().getName() != null) {
                Category existingCat = categoryRepository.findByName(task.getCategory().getName());
                if (existingCat != null) {
                    task.setCategory(existingCat);
                }
            }
        }
        return taskRepository.save(task);
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void markDone(Long id) {
        taskRepository.findById(id).ifPresent(t -> {
            t.setDone(true);
            taskRepository.save(t);
        });
    }

    @Override
    public Page<Task> getTasksWithPagination(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public List<Task> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriorityCustom(priority);
    }

    @Override
    public List<Task> getTasksByStatus(boolean done) {
        return taskRepository.findByDone(done);
    }

    @Override
    public List<Task> getTasksByDueDateRange(LocalDate start, LocalDate end) {
        return taskRepository.findByDueDateBetween(start, end);
    }

    @Override
    public Task partialUpdate(Long id, TaskUpdateDto updateDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (updateDto.getTitle() != null) task.setTitle(updateDto.getTitle());
        if (updateDto.getDescription() != null) task.setDescription(updateDto.getDescription());
        if (updateDto.getDueDate() != null) task.setDueDate(updateDto.getDueDate());
        if (updateDto.getPriority() != null) task.setPriority(updateDto.getPriority());
        if (updateDto.getDone() != null) task.setDone(updateDto.getDone());

        return taskRepository.save(task);
    }
}