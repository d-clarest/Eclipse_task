package com.example.demo.service.routine;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Routine;
import com.example.demo.repository.routine.RoutineRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository repository;

    @Override
    public List<Routine> getAllRoutines() {
        log.debug("Fetching all routines");
        return repository.findAll();
    }

    @Override
    public void addRoutine(Routine routine) {
        log.debug("Adding routine {}", routine.getName());
        repository.insertRoutine(routine);
    }

    @Override
    public void updateRoutine(Routine routine) {
        log.debug("Updating routine id {}", routine.getId());
        repository.updateRoutine(routine);
    }

    @Override
    public void deleteById(int id) {
        log.debug("Deleting routine id {}", id);
        repository.deleteById(id);
    }
}
