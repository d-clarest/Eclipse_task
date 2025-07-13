package com.example.demo.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DreamRecord {
    private int id; // 自動採番ID
    private String dream; // 夢
    private LocalDate completedAt; // 完了日
}
