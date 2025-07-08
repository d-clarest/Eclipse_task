package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Task {
    private int id; // 自動採番ID
    private String title; // タスク名
    private String category; // 区分
    private LocalDateTime deadline; // 締切日時
    private String timeUntilDue; // 期日までを表す文字列
    private String result; // 結果
    private String detail; // 詳細
    private int level; // レベル
    private LocalDateTime createdAt; // 作成日時
    private LocalDateTime updatedAt; // 更新日時
    private LocalDate completedAt; // 完了日
}
