package com.example.taskplanner.web;

import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.Priority;
import com.example.taskplanner.model.TaskUpdateDto;
import com.example.taskplanner.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "APIs for managing tasks")
public class TaskRestController {

    private final TaskService taskService;

    @Autowired
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(
            summary = "Get all tasks",
            description = "Retrieve a list of all tasks with optional sorting and pagination"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Task>> getAllTasks(
            @Parameter(description = "Sort by field: date, priority")
            @RequestParam(required = false) String sort) {
        try {
            List<Task> tasks = taskService.getAll(sort);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paged")
    @Operation(
            summary = "Get tasks with pagination",
            description = "Retrieve tasks with pagination support"
    )
    public ResponseEntity<Page<Task>> getTasksPaged(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "title") String sort) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            Page<Task> tasks = taskService.getTasksWithPagination(pageable);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "Task ID")
            @PathVariable Long id) {
        Optional<Task> task = taskService.getById(id);
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Task> createTask(
            @Parameter(description = "Task object to create")
            @Valid @RequestBody Task task) {
        try {
            Task savedTask = taskService.save(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "Task ID")
            @PathVariable Long id,
            @Parameter(description = "Updated task object")
            @Valid @RequestBody Task task) {
        if (!taskService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        task.setId(id);
        Task updatedTask = taskService.save(task);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Partially update a task",
            description = "Update specific fields of a task (RFC 7386)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task partially updated"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<Task> partialUpdateTask(
            @Parameter(description = "Task ID")
            @PathVariable Long id,
            @Parameter(description = "Fields to update")
            @RequestBody TaskUpdateDto updateDto) {
        try {
            Task updatedTask = taskService.partialUpdate(id, updateDto);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Task ID")
            @PathVariable Long id) {
        if (!taskService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get tasks by priority")
    public ResponseEntity<List<Task>> getTasksByPriority(
            @Parameter(description = "Priority level")
            @PathVariable Priority priority) {
        List<Task> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/{done}")
    @Operation(summary = "Get tasks by completion status")
    public ResponseEntity<List<Task>> getTasksByStatus(
            @Parameter(description = "Completion status")
            @PathVariable boolean done) {
        List<Task> tasks = taskService.getTasksByStatus(done);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/due-date")
    @Operation(summary = "Get tasks by due date range")
    public ResponseEntity<List<Task>> getTasksByDueDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd)")
            @RequestParam String start,
            @Parameter(description = "End date (yyyy-MM-dd)")
            @RequestParam String end) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            List<Task> tasks = taskService.getTasksByDueDateRange(startDate, endDate);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/done")
    @Operation(summary = "Mark task as done")
    public ResponseEntity<Void> markTaskAsDone(
            @Parameter(description = "Task ID")
            @PathVariable Long id) {
        if (!taskService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        taskService.markDone(id);
        return ResponseEntity.ok().build();
    }
}