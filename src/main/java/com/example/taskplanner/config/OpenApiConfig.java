package com.example.taskplanner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskPlannerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Planner API")
                        .description("REST API for Task Management System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@taskplanner.com")));
    }
}