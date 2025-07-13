package com.example.demo.entity;

import lombok.Data;

@Data
public class DreamPage {
    private int id; // 自動採番ID
    private int dreamId; // 対応する夢レコードID
    private String content; // ページ内容
}
