package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.DoubleContest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.DoubleContestDTO;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EliminationDoubleContestFactory extends EliminationContestFactory {

    @Autowired
    private TeamHelper teamHelper;

    @Override
    public Integer createFirstLineEliminationContest(Integer firstParticipantId, Integer secondParticipantId,
                                                     Integer tournamentId, boolean shouldCreateScore) {
        Team firstTeam = teamHelper.getParticipant(firstParticipantId);
        Team secondTeam = teamHelper.getParticipant(secondParticipantId);
        return contestHelper.createNewDoubleContest(firstTeam, secondTeam, tournamentId, shouldCreateScore).getId();
    }

    protected ContestDTO convertEliminationContestToBase(EliminationContestDTO eliminationContest) {
        TeamDTO firstParticipant = getEliminationContestParticipantFromParent(eliminationContest
                .getFirstParentContestDto());
        TeamDTO secondParticipant = getEliminationContestParticipantFromParent(eliminationContest
                .getSecondParentContestDto());
        DoubleContestDTO contestDTO = converterHelper.convert(eliminationContest, DoubleContestDTO.class);
        contestDTO.setTeamOne(firstParticipant);
        contestDTO.setTeamTwo(secondParticipant);
        return contestDTO;
    }

    private TeamDTO getEliminationContestParticipantFromParent(ContestDTO parentContestDTO) {
        Integer winnerId = parentContestDTO.getWinnerId();
        if (parentContestDTO instanceof DoubleContestDTO) {
            if (((DoubleContestDTO) parentContestDTO).getTeamOne().getId().equals(winnerId))
                return ((DoubleContestDTO) parentContestDTO).getTeamOne();
            if (((DoubleContestDTO) parentContestDTO).getTeamTwo().getId().equals(winnerId))
                return ((DoubleContestDTO) parentContestDTO).getTeamTwo();
        }

        EliminationContestDTO parentEliminationContest = (EliminationContestDTO) parentContestDTO;
        if (winnerId.equals(parentEliminationContest.getFirstParentContestDto().getWinnerId()))
            return getEliminationContestParticipantFromParent(parentEliminationContest.getFirstParentContestDto());
        if (winnerId.equals(parentEliminationContest.getSecondParentContestDto().getWinnerId()))
            return getEliminationContestParticipantFromParent(parentEliminationContest.getSecondParentContestDto());
        return null;
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
