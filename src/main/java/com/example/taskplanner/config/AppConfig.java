package com.example.taskplanner.config;

import com.example.taskplanner.service.NotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean
    public String applicationName() {
        return "Task Planner";
    }

    @Bean
    @Scope("prototype")
    public NotificationService prototypeNotificationService() {
        return new NotificationService();
    }
}
