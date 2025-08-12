package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DairyPage {
    private int id; // 自動採番ID
    private int dairyId; // 対応する日記レコードID
    private String content; // ページ内容
    private LocalDateTime createdAt; // 作成日時
    private LocalDateTime updatedAt; // 更新日時
}
