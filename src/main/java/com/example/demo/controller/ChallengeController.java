package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.Challenge;
import com.example.demo.service.challenge.ChallengeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChallengeController {
	private final ChallengeService challengeService;
	
	@PostMapping("/challenge-add")
    public ResponseEntity<Void> addChallenge(@RequestBody Challenge challenge) {
        log.debug("Adding challenge {}", challenge.getTitle());
        challengeService.addChallenge(challenge);
        return ResponseEntity.ok().build();
    }
	
	@PostMapping("/challenge-update")
    public ResponseEntity<Void> updateChallenge(@RequestBody Challenge challenge) {
        log.debug("Updating challenge id {}", challenge.getId());
        challengeService.updateChallenge(challenge);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/challenge-delete")
    public ResponseEntity<Void> deleteChallenge(@RequestBody Challenge challenge) {
        log.debug("Deleting challenge id {}", challenge.getId());
        challengeService.deleteById(challenge.getId());
        return ResponseEntity.ok().build();
    }



}
