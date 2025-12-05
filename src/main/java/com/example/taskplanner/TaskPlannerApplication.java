package com.example.taskplanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.taskplanner.model.Priority;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.repository.TaskRepository;

import java.time.LocalDate;

@SpringBootApplication
public class TaskPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskPlannerApplication.class, args);
    }

    @Bean
    CommandLineRunner initDemo(TaskRepository repo) {
        return args -> {
            repo.save(new Task(null, "Купити продукти", "Купити хліб, молоко", LocalDate.now().plusDays(1), Priority.MEDIUM, false));
            repo.save(new Task(null, "Написати звіт", "Лабораторна робота", LocalDate.now().plusDays(3), Priority.HIGH, false));
            repo.save(new Task(null, "Прочитати книгу", "Розділ 4", LocalDate.now().plusWeeks(1), Priority.LOW, false));
        };
    }
}
