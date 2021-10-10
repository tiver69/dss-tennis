package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerHelper {

    @Autowired
    private PlayerRepository playerRepository;

    public Player createNewPlayer(String firstName, String lastName) {
        Player player = Player.builder().firstName(firstName).lastName(lastName).build();
        return playerRepository.save(player);
    }
}
