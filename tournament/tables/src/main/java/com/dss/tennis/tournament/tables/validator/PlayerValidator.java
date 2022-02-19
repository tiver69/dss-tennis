package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.INSUFFICIENT_PLAYER_QUANTITY;

@Component
public class PlayerValidator {

    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private ValidatorHelper<PlayerDTO> validatorHelper;

    public Set<DetailedErrorData> validatePlayer(PlayerDTO playerDTO) {
        Set<DetailedErrorData> detailedErrorData = validatorHelper.validateObject(playerDTO);

        if (detailedErrorData.isEmpty() && playerHelper.isPlayerNotExist(playerDTO))
            detailedErrorData
                    .add(new DetailedErrorData(ErrorConstants.PLAYER_NOT_FOUND_TOURNAMENT_CREATION, playerDTO.getSequenceNumber()));
        return detailedErrorData;
    }

    public Optional<DetailedErrorData> validatePlayerQuantity(List<PlayerDTO> players) {
        return players.size() <= 1 ? Optional.of(new DetailedErrorData(INSUFFICIENT_PLAYER_QUANTITY)) : Optional
                .empty();
    }
}
