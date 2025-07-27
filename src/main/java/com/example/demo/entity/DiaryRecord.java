package com.example.demo.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DiaryRecord {
    private int id;               // ID
    private LocalDate recordDate; // 記入日
    private String content;       // 日記内容
}
