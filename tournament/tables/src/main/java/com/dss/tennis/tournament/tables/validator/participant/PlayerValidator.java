package com.dss.tennis.tournament.tables.validator.participant;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import com.dss.tennis.tournament.tables.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.*;

@Component
public class PlayerValidator extends ParticipantValidator<Player> {

    @Autowired
    private ValidatorHelper<PlayerDTO> validatorHelper;

    @Override
    public ErrorDataDTO validateParticipantForEnrolling(List<Integer> currentPlayerIds, ResourceObjectDTO newPlayer) {
        Integer participantId = newPlayer.getId();

        if (ResourceObjectType.PLAYER != newPlayer.getType())
            return new ErrorDataDTO(UNSUPPORTED_RESOURCE_TYPE, newPlayer.getType().value, newPlayer
                    .getSequenceNumber());
        if (playerHelper.isParticipantNotExist(participantId))
            return new ErrorDataDTO(PLAYER_NOT_FOUND, participantId.toString(), newPlayer
                    .getSequenceNumber());
        if (currentPlayerIds.contains(participantId))
            return new ErrorDataDTO(PARTICIPANT_DUPLICATION, participantId.toString(), newPlayer
                    .getSequenceNumber());
        return null;
    }

    @Override
    public void validateParticipantForRemoving(Integer playerId, Integer tournamentId) {
        List<Integer> playerIds = playerHelper.getTournamentPlayerIds(tournamentId);

        if (!playerIds.contains(playerId)) throw new DetailedException(PARTICIPANT_NOT_FOUND, playerId);
        if (playerIds.size() <= 2) throw new DetailedException(FORBIDDEN_PARTICIPANT_QUANTITY_REMOVING, tournamentId);
    }

    public Set<ErrorDataDTO> validatePlayer(PlayerDTO playerDTO) {
        Set<ErrorDataDTO> detailedErrorData = validatorHelper.validateObject(playerDTO);

        if (detailedErrorData.isEmpty() && playerHelper.isPlayerExist(playerDTO))
            detailedErrorData.add(new ErrorDataDTO(ErrorConstants.PLAYER_DUPLICATION));
        if (playerDTO.getBirthDate() != null && LocalDate.now().isBefore(playerDTO.getBirthDate()))
            detailedErrorData.add(new ErrorDataDTO(ErrorConstants.BIRTH_DATE_ILLEGAL));
        if (playerDTO.getExperienceYear() != null && LocalDate.now().getYear() < playerDTO.getExperienceYear())
            detailedErrorData.add(new ErrorDataDTO(ErrorConstants.EXPERIENCE_YEAR_ILLEGAL));

        return detailedErrorData;
    }
}
