package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.model.v1.Player;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/players")
@CrossOrigin
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        playerRepository.save(player);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> findPlayerById(@PathVariable Integer playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        player.ifPresent(System.out::println);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
