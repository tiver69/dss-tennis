package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.request.CreateTournament;
import com.dss.tennis.tournament.tables.service.TournamentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tournaments")
@CrossOrigin
public class TournamentController {

    @Autowired
    TournamentService tournamentService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<?> createTournament(@RequestBody CreateTournament tournament) {
        CreateTournamentDTO createTournamentDto = modelMapper.map(tournament, CreateTournamentDTO.class);
        tournamentService.createNewTournament(createTournamentDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<?> findTournamentById(@PathVariable Integer tournamentId) {
//        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
//        tournament.ifPresent(System.out::println);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
