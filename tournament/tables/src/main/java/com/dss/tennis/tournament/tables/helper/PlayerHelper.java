package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.apache.commons.lang3.StringUtils;
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

    public boolean isPlayerNotExist(PlayerDTO playerDTO) {
        return !isPlayerExist(playerDTO);
    }

    public boolean isPlayerExist(PlayerDTO playerDTO) {
        if (StringUtils.isBlank(playerDTO.getFirstName()) || StringUtils.isBlank(playerDTO.getLastName()))
            return false;
        return playerRepository
                .findByFirstNameAndLastName(playerDTO.getFirstName(), playerDTO.getLastName()).isPresent();
    }

    public Player createNewPlayer(String firstName, String lastName) {
        Player player = Player.builder().firstName(firstName).lastName(lastName).build();
        return playerRepository.save(player);
    }
}
