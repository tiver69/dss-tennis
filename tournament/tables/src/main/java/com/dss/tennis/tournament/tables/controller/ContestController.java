package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.model.v1.Contest;
import com.dss.tennis.tournament.tables.model.v1.Score;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import com.dss.tennis.tournament.tables.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/matches")
@CrossOrigin
public class ContestController {

    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    @PostMapping
    public ResponseEntity<?> createContest(@RequestBody Contest contest) {
        contestRepository.save(contest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{contestId}")
    public ResponseEntity<?> findContestById(@PathVariable Integer contestId) {
        Optional<Contest> contest = contestRepository.findById(contestId);
        contest.ifPresent(contest1 -> System.out.println(contest1.toString()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
