package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SubTask {
    private int id; // 自動採番ID
    private int taskId; // 親タスクID
    private String title; // 子タスク名
    private LocalDateTime deadline; // 締切
    private String timeUntilDue; // 期日
    private LocalDate completedAt; // 完了日
    private boolean expired; // 期限切れかどうか
}
