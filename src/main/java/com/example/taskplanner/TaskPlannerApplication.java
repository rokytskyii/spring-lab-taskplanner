package com.example.taskplanner;

import com.example.taskplanner.model.Category;
import com.example.taskplanner.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionTemplate;

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
    CommandLineRunner initDemo(TaskRepository taskRepo, CategoryRepository categoryRepo, TransactionTemplate txTemplate) {
        return args -> {
            txTemplate.execute(status -> {
                if (taskRepo.findAll().isEmpty()) {
                    Category catShopping = categoryRepo.findByName("Покупки");
                    if (catShopping == null) catShopping = categoryRepo.save(new Category("Покупки"));

                    Category catWork = categoryRepo.findByName("Робота");
                    if (catWork == null) catWork = categoryRepo.save(new Category("Робота"));

                    Category catStudy = categoryRepo.findByName("Навчання");
                    if (catStudy == null) catStudy = categoryRepo.save(new Category("Навчання"));

                    taskRepo.save(new Task(null, "Купити продукти", "Хліб, молоко", LocalDate.now().plusDays(1), Priority.MEDIUM, false, catShopping));
                    taskRepo.save(new Task(null, "Написати звіт", "Лабораторна 6", LocalDate.now().plusDays(3), Priority.HIGH, false, catStudy)); // Змінив на Навчання
                    taskRepo.save(new Task(null, "Прочитати книгу", "Розділ 4", LocalDate.now().plusWeeks(1), Priority.LOW, false, catWork)); // Змінив на Робота
                }
                return null;
            });
        };
    }
}