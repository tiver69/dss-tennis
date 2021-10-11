package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class PlayerValidator {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ValidatorHelper<PlayerDTO> validatorHelper;

    public Set<DetailedErrorData> validatePlayer(PlayerDTO playerDTO) {
        Set<DetailedErrorData> detailedErrorData = validatorHelper.validateObject(playerDTO);

        Optional<Player> player = playerRepository
                .findByFirstNameAndLastName(playerDTO.getFirstName(), playerDTO.getLastName());
        if (!player.isPresent()) detailedErrorData
                .add(new DetailedErrorData(ErrorConstants.PLAYER_NOT_FOUND, playerDTO.getSequenceNumber()));

        return detailedErrorData;
    }
}
