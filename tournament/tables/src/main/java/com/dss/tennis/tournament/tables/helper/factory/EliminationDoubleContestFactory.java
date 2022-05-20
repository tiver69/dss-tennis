package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.DoubleContest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.DoubleContestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EliminationDoubleContestFactory extends EliminationContestFactory {

    @Autowired
    private TeamHelper teamHelper;

    @Override
    public Integer createFirstLineEliminationContest(Integer firstParticipantId, Integer secondParticipantId,
                                                     Integer tournamentId) {
        Team firstTeam = teamHelper.getParticipant(firstParticipantId);
        Team secondTeam = teamHelper.getParticipant(secondParticipantId);
        return contestHelper.createNewDoubleContest(firstTeam, secondTeam, tournamentId).getId();
    }

    @Override
    public Class<? extends ContestDTO> getContestParticipantDtoClass() {
        return DoubleContestDTO.class;
    }

    @Override
    public Class<? extends Contest> getContestParticipantClass() {
        return DoubleContest.class;
    }
}
