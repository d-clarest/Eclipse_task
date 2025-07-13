package com.example.demo.repository.routine;

import java.util.List;
import com.example.demo.entity.Routine;

public interface RoutineRepository {
    List<Routine> findAll();
    void insertRoutine(Routine routine);
    void updateRoutine(Routine routine);
    void deleteById(int id);
}
