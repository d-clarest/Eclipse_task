package com.example.demo.entity;

import lombok.Data;

@Data
public class TaskPage {
    private int id; // 自動採番ID
    private int taskId; // 対応するタスクID
    private String content; // ページ内容
}
