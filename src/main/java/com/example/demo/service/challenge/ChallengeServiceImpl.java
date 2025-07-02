package com.example.demo.service.challenge;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Challenge;
import com.example.demo.repository.challenge.ChallengeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository repository;

    @Override
    public List<Challenge> getAllChallenges() {
        log.debug("Fetching all challenges");
        return repository.findAll();
    }

    @Override
    public void addChallenge(Challenge challenge) {
        log.debug("Adding challenge {}", challenge.getTitle());
        repository.insertChallenge(challenge);
    }

    @Override
    public void updateChallenge(Challenge challenge) {
        log.debug("Updating challenge id {}", challenge.getId());
        repository.updateChallenge(challenge);
    }

    @Override
    public void deleteById(int id) {
        log.debug("Deleting challenge with id {}", id);
        repository.deleteById(id);
    }

    @Override
    public int getTotalCompletedPoints() {
        log.debug("Fetching total challenge points");
        return repository.sumCompletedPoints();
    }
}
