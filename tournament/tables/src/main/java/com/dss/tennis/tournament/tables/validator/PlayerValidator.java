package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import com.dss.tennis.tournament.tables.validator.error.ErrorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlayerValidator {

    @Autowired
    private PlayerRepository playerRepository;

    public Optional<String> validatePlayerName(String name) {
        //todo: change !=2
        if (name == null || name.trim().isEmpty() || !name.contains(" ") || name.split(" ").length != 2)
            return Optional.of(ErrorConstants.PLAYER_FIRST_OR_LAST_NAME_EMPTY);
        return Optional.empty();
    }
}
