package com.example.taskplanner.repository;

import com.example.taskplanner.model.Priority;
import com.example.taskplanner.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByDone(boolean done);

    List<Task> findByDueDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> searchByTitle(@Param("keyword") String keyword);

    @Query(name = "Task.findByPriorityNamed")
    List<Task> findByPriorityCustom(@Param("priority") Priority priority);
}