package com.example.taskplanner;

import com.example.taskplanner.model.Category;
import com.example.taskplanner.repository.CategoryRepository;
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
    CommandLineRunner initDemo(TaskRepository taskRepo, CategoryRepository categoryRepo) {
        return args -> {
            if (taskRepo.findAll().isEmpty()) {

                Category defaultCategory = new Category("Загальне");
                if (categoryRepo.count() == 0) {
                    defaultCategory = categoryRepo.save(defaultCategory);
                } else {
                    defaultCategory = categoryRepo.findAll().get(0);
                }

                // 2. Тепер передаємо об'єкт defaultCategory як 7-й аргумент у конструктор
                taskRepo.save(new Task(null, "Купити продукти", "Купити хліб, молоко",
                        LocalDate.now().plusDays(1), Priority.MEDIUM, false, defaultCategory));

                taskRepo.save(new Task(null, "Написати звіт", "Лабораторна робота",
                        LocalDate.now().plusDays(3), Priority.HIGH, false, defaultCategory));

                taskRepo.save(new Task(null, "Прочитати книгу", "Розділ 4",
                        LocalDate.now().plusWeeks(1), Priority.LOW, false, defaultCategory));
            }
        };
    }
}