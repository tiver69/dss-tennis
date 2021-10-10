package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.PLAYER_FIRST_OR_LAST_NAME_EMPTY;

@Component
public class PlayerValidator {

    @Autowired
    private PlayerRepository playerRepository;

    public Optional<DetailedErrorData> validatePlayerName(PlayerDTO player) {
        //todo: change !=2
        if (player == null || StringUtils.isBlank(player.getFirstName()) || StringUtils.isBlank(player.getLastName()))
            return Optional.of(new DetailedErrorData(PLAYER_FIRST_OR_LAST_NAME_EMPTY, player.getSequenceNumber()));
        return Optional.empty();
    }
}
