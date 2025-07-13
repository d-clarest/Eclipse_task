package com.example.demo.entity;

import java.time.LocalTime;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Routine {
    private int id;       // ID
    private String name;  // ルーティン名
    private String type;  // 区分（予定・タスク・挑戦）
    private String frequency; // 頻度（毎日・毎週・毎月）
    private LocalDate startDate; // 開始日
    private LocalTime timing; // タイミング
}
