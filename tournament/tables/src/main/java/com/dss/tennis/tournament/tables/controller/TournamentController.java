package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.request.CreateTournament;
import com.dss.tennis.tournament.tables.model.response.v1.GetTournament;
import com.dss.tennis.tournament.tables.service.TournamentService;
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
    ConverterHelper converterHelper;

    @PostMapping
    public ResponseEntity<GetTournament> createTournament(@RequestBody CreateTournament tournament) {
        TournamentDTO tournamentDto = converterHelper.convert(tournament, TournamentDTO.class, true);

        TournamentDTO tournamentDTO = tournamentService.createNewTournament(tournamentDto);
        GetTournament tournamentResponse = converterHelper.convert(tournamentDTO, GetTournament.class);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<GetTournament> findTournamentById(@PathVariable Integer tournamentId) {
        TournamentDTO tournamentDTO = tournamentService.getTournament(tournamentId);

        GetTournament tournamentResponse = converterHelper.convert(tournamentDTO, GetTournament.class);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.OK);
    }
}
