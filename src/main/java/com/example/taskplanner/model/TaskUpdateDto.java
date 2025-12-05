package com.example.taskplanner.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskUpdateDto {
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private Boolean done;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Boolean getDone() { return done; }
    public void setDone(Boolean done) { this.done = done; }
}