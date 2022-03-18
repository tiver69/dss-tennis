package com.dss.tennis.tournament.tables.validator.participant;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import com.dss.tennis.tournament.tables.exception.error.WarningConstant;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import com.dss.tennis.tournament.tables.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.WarningConstant.UNSUPPORTED_RESOURCE_TYPE;

@Component
public class PlayerValidator extends ParticipantValidator<Player> {

    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private ValidatorHelper<PlayerDTO> validatorHelper;

    @Override
    public ErrorData validateParticipantForEnrolling(List<Integer> currentPlayerIds,
                                                     ResourceObjectDTO newPlayer) {
        Integer participantId = newPlayer.getId();

        if (ResourceObjectType.PLAYER != newPlayer.getType())
            return warningHandler
                    .createWarning(UNSUPPORTED_RESOURCE_TYPE, newPlayer.getType().value, newPlayer
                            .getSequenceNumber());
        if (playerHelper.isParticipantNotExist(participantId))
            return warningHandler
                    .createWarning(WarningConstant.PLAYER_NOT_FOUND, participantId.toString(), newPlayer
                            .getSequenceNumber());
        if (currentPlayerIds.contains(participantId))
            return warningHandler
                    .createWarning(WarningConstant.PARTICIPANT_DUPLICATION, participantId.toString(), newPlayer
                            .getSequenceNumber());
        return null;
    }

    public Set<DetailedErrorData> validateNewPlayer(PlayerDTO playerDTO) {
        Set<DetailedErrorData> detailedErrorData = validatorHelper.validateObject(playerDTO);

        if (detailedErrorData.isEmpty() && playerHelper.isPlayerExist(playerDTO))
            detailedErrorData.add(new DetailedErrorData(ErrorConstants.PLAYER_DUPLICATION));
        return detailedErrorData;
    }
}
