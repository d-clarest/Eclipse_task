package com.example.demo.entity;

import lombok.Data;

@Data
public class WordRecord {
    private int id;      // ID
    private String word; // 単語
    private String meaning; // 意味
    private String example; // 例文
    private int count;//ミス数
}
