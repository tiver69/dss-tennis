package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import com.dss.tennis.tournament.tables.exception.error.WarningConstant;
import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.FORBIDDEN_PLAYER_QUANTITY;
import static com.dss.tennis.tournament.tables.exception.error.WarningConstant.UNSUPPORTED_RESOURCE_TYPE;

@Component
public class PlayerValidator {

    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private ValidatorHelper<PlayerDTO> validatorHelper;
    @Autowired
    private WarningHandler warningHandler;

    public Set<DetailedErrorData> validateNewPlayer(PlayerDTO playerDTO) {
        Set<DetailedErrorData> detailedErrorData = validatorHelper.validateObject(playerDTO);

        if (detailedErrorData.isEmpty() && playerHelper.isPlayerExist(playerDTO))
            detailedErrorData.add(new DetailedErrorData(ErrorConstants.PLAYER_DUPLICATION));
        return detailedErrorData;
    }

    public Set<DetailedErrorData> validatePlayer(PlayerDTO playerDTO) {
        Set<DetailedErrorData> detailedErrorData = validatorHelper.validateObject(playerDTO);

        if (detailedErrorData.isEmpty() && playerHelper.isPlayerNotExist(playerDTO))
            detailedErrorData
                    .add(new DetailedErrorData(ErrorConstants.PLAYER_NOT_FOUND_TOURNAMENT_CREATION, playerDTO
                            .getSequenceNumber()));
        return detailedErrorData;
    }

    public ErrorData validatePlayerForEnrolling(List<Integer> currentPlayers, ResourceObjectDTO newParticipant,
                                                ResourceObjectType requiredType) {
        Integer participantId = newParticipant.getId();

        if (requiredType != newParticipant.getType())
            return warningHandler
                    .createWarning(UNSUPPORTED_RESOURCE_TYPE, newParticipant.getType().value, newParticipant
                            .getSequenceNumber());
        if (playerHelper.isPlayerNotExist(participantId))
            return warningHandler
                    .createWarning(WarningConstant.PLAYER_NOT_FOUND, participantId.toString(), newParticipant
                            .getSequenceNumber());
        if (currentPlayers.contains(participantId))
            return warningHandler
                    .createWarning(WarningConstant.PLAYER_DUPLICATION, participantId.toString(), newParticipant
                            .getSequenceNumber());
        return null;
    }

    public void validatePlayerQuantity(int playersSize) {
        if (playersSize <= 2 || playersSize > 20)
            throw new DetailedException(FORBIDDEN_PLAYER_QUANTITY, playersSize);
    }

    public Optional<DetailedErrorData> validatePlayerQuantity(List<PlayerDTO> players) {
        return players.size() <= 1 ? Optional.of(new DetailedErrorData(FORBIDDEN_PLAYER_QUANTITY)) : Optional
                .empty();
    }
}
