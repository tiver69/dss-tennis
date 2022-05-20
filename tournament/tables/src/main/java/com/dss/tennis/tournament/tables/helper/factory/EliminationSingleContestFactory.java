package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EliminationSingleContestFactory extends EliminationContestFactory {

    @Autowired
    private PlayerHelper playerHelper;

    @Override
    public Integer createFirstLineEliminationContest(Integer firstParticipantId, Integer secondParticipantId,
                                                     Integer tournamentId) {
        return contestHelper.createNewSingleContest(firstParticipantId, secondParticipantId, tournamentId)
                .getId();
    }

    @Override
    public Class<? extends ContestDTO> getContestParticipantDtoClass() {
        return SingleContestDTO.class;
    }

    @Override
    public Class<? extends Contest> getContestParticipantClass() {
        return SingleContest.class;
    }
}
