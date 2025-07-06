package com.example.demo.entity;

import lombok.Data;

@Data
public class AwarenessRecord {
    private int id; // ID
    private String awareness; // 気づき
    private String opinion; // 意見
    private int awarenessLevel; // 気づき度
}
