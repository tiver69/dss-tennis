package com.dss.tennis.tournament.tables.validator.participant;

import com.dss.tennis.tournament.tables.exception.error.WarningConstant;
import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dss.tennis.tournament.tables.exception.error.WarningConstant.UNSUPPORTED_RESOURCE_TYPE;

@Component
public class TeamValidator extends ParticipantValidator<Team> {

    @Autowired
    private TeamHelper teamHelper;

    @Override
    public ErrorData validateParticipantForEnrolling(List<Integer> currentPlayerIds, ResourceObjectDTO newTeam) {

        Integer newTeamId = newTeam.getId();
        if (ResourceObjectType.TEAM != newTeam.getType())
            return warningHandler
                    .createWarning(UNSUPPORTED_RESOURCE_TYPE, newTeam.getType().value, newTeam
                            .getSequenceNumber());
        Team team = teamHelper.getParticipant(newTeamId);
        if (team == null) {
            return warningHandler
                    .createWarning(WarningConstant.TEAM_NOT_FOUND, newTeamId.toString(), newTeam
                            .getSequenceNumber());
        }
        if (currentPlayerIds.contains(team.getPlayerOneId()) || currentPlayerIds.contains(team.getPlayerTwoId())) {
            return warningHandler
                    .createWarning(WarningConstant.PARTICIPANT_DUPLICATION, newTeamId.toString(), newTeam
                            .getSequenceNumber());
        }
        return null;
    }
}
