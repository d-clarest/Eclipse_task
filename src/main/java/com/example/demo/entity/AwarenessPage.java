package com.example.demo.entity;

import lombok.Data;

@Data
public class AwarenessPage {
    private int id; // 自動採番ID
    private int recordId; // 対応する気づきレコードID
    private String content; // 日記内容
}
