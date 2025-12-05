package com.example.taskplanner.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class NotificationService {

    public void notify(String message) {
        System.out.println("[NotificationService (prototype)] " + message);
    }
}
