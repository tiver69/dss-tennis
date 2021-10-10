package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PlayerValidator {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ValidatorHelper<PlayerDTO> validatorHelper;

    public Set<DetailedErrorData> validatePlayerName(PlayerDTO player) {
        return validatorHelper.validateObject(player);
    }
}
