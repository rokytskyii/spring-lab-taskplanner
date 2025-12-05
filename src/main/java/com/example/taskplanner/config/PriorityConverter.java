package com.example.taskplanner.config;

import com.example.taskplanner.model.Priority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PriorityConverter implements Converter<String, Priority> {

    @Override
    public Priority convert(String source) {
        try {
            return Priority.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Priority.MEDIUM;
        }
    }
}