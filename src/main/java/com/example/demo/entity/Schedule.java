package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Schedule {
    private int id;                  // ID
    private boolean addFlag;         // 予定追加 ON/OFF
    private String title;            // 予定名
    private String dayOfWeek;        // 曜日
    private LocalDate scheduleDate;  // 予定日
    private LocalTime startTime;     // 開始時刻
    private LocalTime endTime;       // 終了時刻
    private String location;         // 場所
    private String detail;           // 詳細
    private String feedback;
    private int point;               // ポイント
    private LocalDate completedDay;  // 完了日（null 可）
    private String timeUntilStart;   // 開始時刻までを表す文字列
}
