package com.example.demo.service.challenge;

import java.util.List;

import com.example.demo.entity.Challenge;

public interface ChallengeService {
    List<Challenge> getAllChallenges();
    void addChallenge(Challenge challenge);
    void updateChallenge(Challenge challenge);
    void deleteById(int id);
    int getTotalCompletedPoints();
}
