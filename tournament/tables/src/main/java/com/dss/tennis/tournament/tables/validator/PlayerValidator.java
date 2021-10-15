package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

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
                    .add(new DetailedErrorData(ErrorConstants.PLAYER_NOT_FOUND, playerDTO.getSequenceNumber()));
        return detailedErrorData;
    }
}
