package com.example.demo.form;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class ChallengeUpdateForm {
    private int id;

    private String title;                 // 予定名
    private String risk;                  // リスクの説明
    private String expectedResult;        // 目指す結果
    private String strategy;              // 実行する戦略
    private String actualResult;          // 実際の結果
    private String improvementPlan;       // 改善策
    private int challengeLevel;           // 難易度・挑戦度
    private LocalDate challengeDate;      // 挑戦を始めた日
}
