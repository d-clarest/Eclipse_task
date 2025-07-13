package com.example.demo.service.routine;

import java.util.List;
import com.example.demo.entity.Routine;

public interface RoutineService {
    List<Routine> getAllRoutines();
    void addRoutine(Routine routine);
    void updateRoutine(Routine routine);
    void deleteById(int id);
}
