package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerHelper {

    @Autowired
    private PlayerRepository playerRepository;

    public Player getPlayer(PlayerDTO playerDTO) {
        Optional<Player> repositoryPlayer = playerRepository
                .findByFirstNameAndLastName(playerDTO.getFirstName(), playerDTO.getLastName());
        if (repositoryPlayer.isPresent()) return repositoryPlayer.get();
        throw new DetailedException(ErrorConstants.PLAYER_NOT_FOUND, playerDTO.getSequenceNumber());
    }

    public Player createNewPlayer(String firstName, String lastName) {
        Player player = Player.builder().firstName(firstName).lastName(lastName).build();
        return playerRepository.save(player);
    }
}
