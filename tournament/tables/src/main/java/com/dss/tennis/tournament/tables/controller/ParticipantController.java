package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetPlayer;
import com.dss.tennis.tournament.tables.model.response.v1.SuccessResponse;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import com.dss.tennis.tournament.tables.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/participants")
@CrossOrigin
public class ParticipantController {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    private ResponseHelper responseHelper;
    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        playerRepository.save(player);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/players/{playerId}")
    public ResponseEntity<SuccessResponse<GetPlayer>> getPlayerById(@PathVariable Integer playerId) {
        PlayerDTO player = participantService.getPlayer(playerId);

        SuccessResponse<GetPlayer> playerResponse = responseHelper.createSuccessResponse(player, GetPlayer.class);
        return new ResponseEntity<>(playerResponse, HttpStatus.OK);
    }
}
