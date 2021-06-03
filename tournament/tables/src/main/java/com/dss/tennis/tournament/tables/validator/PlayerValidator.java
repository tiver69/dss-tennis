package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.PLAYER_FIRST_OR_LAST_NAME_EMPTY;

@Component
public class PlayerValidator {

    @Autowired
    private PlayerRepository playerRepository;

    public Optional<DetailedErrorData> validatePlayerName(String name) {
        //todo: change !=2
        if (name == null || name.trim().isEmpty() || !name.contains(" ") || name.split(" ").length != 2)
            return Optional.of(new DetailedErrorData(PLAYER_FIRST_OR_LAST_NAME_EMPTY));
        return Optional.empty();
    }
}
