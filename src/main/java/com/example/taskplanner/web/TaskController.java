package com.example.taskplanner.web;

import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.Priority;
import com.example.taskplanner.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String sort, Model model) {
        List<Task> tasks = taskService.getAll(sort);

        System.out.println("=== CONTROLLER - TASKS LIST ===");
        for (Task task : tasks) {
            System.out.println("Task: " + task.getTitle() +
                    ", Priority: " + task.getPriority() +
                    ", ID: " + task.getId());
        }

        model.addAttribute("tasks", taskService.getAll(sort));
        model.addAttribute("sort", sort);
        return "list";
    }

    @GetMapping("task/new")
    public String createForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", Priority.values());
        return "form";
    }

    @GetMapping("task/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Task> t = taskService.getById(id);
        if (t.isPresent()) {
            model.addAttribute("task", t.get());
            model.addAttribute("priorities", Priority.values());
            return "form";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("task/save")
    public String save(@ModelAttribute Task task) {
        System.out.println("Збереження задачі: " + task.getTitle());
        System.out.println("Пріоритет: " + task.getPriority());
        System.out.println("Статус: " + task.isDone());

        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("task/delete/{id}")
    public String delete(@PathVariable Long id) {
        taskService.delete(id);
        return "redirect:/";
    }

    @GetMapping("task/done/{id}")
    public String markDone(@PathVariable Long id) {
        taskService.markDone(id);
        return "redirect:/";
    }
}
