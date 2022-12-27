package ua.com.dss.tennis.tournament.api.validator.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants;
import ua.com.dss.tennis.tournament.api.helper.participant.TeamHelper;
import ua.com.dss.tennis.tournament.api.model.db.v1.Team;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ResourceObjectDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TeamDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;
import ua.com.dss.tennis.tournament.api.validator.ValidatorHelper;

import java.util.List;
import java.util.Set;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.*;
import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TEAM;

@Component
public class TeamValidator extends ParticipantValidator<Team> {

    @Autowired
    private TeamHelper teamHelper;
    @Autowired
    private ValidatorHelper<TeamDTO> validatorHelper;

    @Override
    public void validateTournamentParticipantQuantity(TournamentDTO tournamentDto, int additionalParticipantQuantity) {
        int currentParticipantQuantity = teamHelper.getTournamentParticipants(tournamentDto.getId()).size();
        tournamentFactory.getParticipantEnrollingQuantityValidationRule(tournamentDto.getTournamentType(), tournamentDto
                .getParticipantType()).accept(currentParticipantQuantity + additionalParticipantQuantity);
    }

    @Override
    public ErrorDataDTO validateParticipantForEnrolling(List<Integer> currentPlayerIds, ResourceObjectDTO newTeam) {
        Integer newTeamId = newTeam.getId();
        if (TEAM != newTeam.getType())
            return new ErrorDataDTO(UNSUPPORTED_RESOURCE_TYPE, newTeam.getType().value, newTeam
                    .getSequenceNumber());

        if (teamHelper.isParticipantNotExist(newTeamId))
            return new ErrorDataDTO(ErrorConstants.ErrorKey.TEAM_NOT_FOUND, newTeamId.toString(), newTeam
                    .getSequenceNumber());
        Team team = teamHelper.getParticipant(newTeamId);
        if (currentPlayerIds.contains(team.getPlayerOneId()) || currentPlayerIds.contains(team.getPlayerTwoId()))
            return new ErrorDataDTO(ErrorConstants.ErrorKey.PARTICIPANT_DUPLICATION, newTeamId.toString(), newTeam
                    .getSequenceNumber());

        return null;
    }

    @Override
    public void validateParticipantForRemoving(Integer teamId, Integer tournamentId) {
        List<Integer> teamIds = teamHelper.getTournamentTeamIds(tournamentId);

        if (!teamIds.contains(teamId)) throw new DetailedException(PARTICIPANT_NOT_FOUND, teamId);
        if (teamIds.size() <= 2) throw new DetailedException(FORBIDDEN_PARTICIPANT_QUANTITY_REMOVING, tournamentId);
    }

    public Set<ErrorDataDTO> validateTeamCreation(TeamDTO teamDto) {
        Set<ErrorDataDTO> detailedErrorData = validatorHelper.validateObject(teamDto);
        if (!detailedErrorData.isEmpty()) return detailedErrorData;

        if (teamHelper.isTeamExist(teamDto.getPlayerOne().getId(), teamDto.getPlayerTwo().getId()))
            throw new DetailedException(TEAM_DUPLICATION);

        int playerOneId = teamDto.getPlayerOne().getId();
        int playerTwoId = teamDto.getPlayerTwo().getId();
        if (playerHelper.isParticipantNotExist(playerOneId)) {
            detailedErrorData.add(new ErrorDataDTO(TEAM_PLAYER_NOT_FOUND, String.valueOf(playerOneId),(byte)0));
        }
        if (playerHelper.isParticipantNotExist(playerTwoId)) {
            detailedErrorData.add(new ErrorDataDTO(TEAM_PLAYER_NOT_FOUND, String.valueOf(playerTwoId),(byte)1));
        }
        if (playerOneId == playerTwoId)
            detailedErrorData.add(new ErrorDataDTO(TEAM_PLAYER_DUPLICATION, String.valueOf(playerTwoId)));
        return detailedErrorData;
    }
}
