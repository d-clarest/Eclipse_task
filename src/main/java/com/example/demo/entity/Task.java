package com.example.demo.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Task {
    private String taskName;
    private LocalDate dueDate;
    private boolean confirmed;
}
