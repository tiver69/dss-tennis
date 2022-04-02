package com.dss.tennis.tournament.tables.validator.participant;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.*;

@Component
public class TeamValidator extends ParticipantValidator<Team> {

    @Autowired
    private TeamHelper teamHelper;

    @Override
    public ErrorDataDTO validateParticipantForEnrolling(List<Integer> currentPlayerIds, ResourceObjectDTO newTeam) {
        Integer newTeamId = newTeam.getId();
        if (ResourceObjectType.TEAM != newTeam.getType())
            return new ErrorDataDTO(UNSUPPORTED_RESOURCE_TYPE, newTeam.getType().value, newTeam
                    .getSequenceNumber());

        Team team = teamHelper.getParticipant(newTeamId);
        if (team == null)
            return new ErrorDataDTO(ErrorConstants.TEAM_NOT_FOUND, newTeamId.toString(), newTeam
                    .getSequenceNumber());
        if (currentPlayerIds.contains(team.getPlayerOneId()) || currentPlayerIds.contains(team.getPlayerTwoId()))
            return new ErrorDataDTO(ErrorConstants.PARTICIPANT_DUPLICATION, newTeamId.toString(), newTeam
                    .getSequenceNumber());

        return null;
    }

    @Override
    public void validateParticipantForRemoving(Integer teamId, Integer tournamentId) {
        List<Integer> teamIds = teamHelper.getTournamentTeamIds(tournamentId);

        if (!teamIds.contains(teamId)) throw new DetailedException(PARTICIPANT_NOT_FOUND, teamId);
        if (teamIds.size() <= 2) throw new DetailedException(FORBIDDEN_PARTICIPANT_QUANTITY_REMOVING, tournamentId);
    }
}
