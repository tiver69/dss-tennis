package ua.com.dss.tennis.tournament.api.validator.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants;
import ua.com.dss.tennis.tournament.api.model.db.v1.Player;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ResourceObjectDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;
import ua.com.dss.tennis.tournament.api.validator.ValidatorHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.*;
import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.PLAYER;

@Component
public class PlayerValidator extends ParticipantValidator<Player> {

    @Autowired
    private ValidatorHelper<PlayerDTO> validatorHelper;

    @Override
    public void validateTournamentParticipantQuantity(TournamentDTO tournamentDto, int additionalParticipantQuantity) {
        int currentParticipantQuantity = playerHelper.getTournamentParticipants(tournamentDto.getId()).size();
        tournamentFactory.getParticipantEnrollingQuantityValidationRule(tournamentDto.getTournamentType(), tournamentDto
                .getParticipantType()).accept(currentParticipantQuantity + additionalParticipantQuantity);
    }

    @Override
    public ErrorDataDTO validateParticipantForEnrolling(List<Integer> currentPlayerIds, ResourceObjectDTO newPlayer) {
        Integer participantId = newPlayer.getId();

        if (PLAYER != newPlayer.getType())
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
            detailedErrorData.add(new ErrorDataDTO(ErrorConstants.ErrorKey.PLAYER_DUPLICATION));
        if (playerDTO.getBirthDate() != null && LocalDate.now().isBefore(playerDTO.getBirthDate()))
            detailedErrorData.add(new ErrorDataDTO(ErrorConstants.ErrorKey.BIRTH_DATE_ILLEGAL));
        if (playerDTO.getExperienceYear() != null && LocalDate.now().getYear() < playerDTO.getExperienceYear())
            detailedErrorData.add(new ErrorDataDTO(ErrorConstants.ErrorKey.EXPERIENCE_YEAR_ILLEGAL));

        return detailedErrorData;
    }
}
