package com.example.demo.repository.challenge;

import java.util.List;

import com.example.demo.entity.Challenge;

public interface ChallengeRepository {
    List<Challenge> findAll();
    void insertChallenge(Challenge challenge);
    void updateChallenge(Challenge challenge);
    void deleteById(int id);
    int sumCompletedPoints();
}
