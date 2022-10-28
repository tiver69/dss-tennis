package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EliminationSingleContestFactory extends EliminationContestFactory {

    @Autowired
    private PlayerHelper playerHelper;

    @Override
    public Integer createFirstLineEliminationContest(Integer firstParticipantId, Integer secondParticipantId,
                                                     Integer tournamentId, boolean shouldCreateScore) {
        return contestHelper.createNewSingleContest(firstParticipantId, secondParticipantId, tournamentId, shouldCreateScore)
                .getId();
    }

    @Override
    protected ContestDTO convertEliminationContestToBase(EliminationContestDTO eliminationContest) {
        PlayerDTO firstParticipant = getEliminationContestParticipantFromParent(eliminationContest
                .getFirstParentContestDto());
        PlayerDTO secondParticipant = getEliminationContestParticipantFromParent(eliminationContest
                .getSecondParentContestDto());
        SingleContestDTO contestDTO = converterHelper.convert(eliminationContest, SingleContestDTO.class);
        contestDTO.setPlayerOne(firstParticipant);
        contestDTO.setPlayerTwo(secondParticipant);
        return contestDTO;
    }

    private PlayerDTO getEliminationContestParticipantFromParent(ContestDTO parentContestDTO) {
        Integer winnerId = parentContestDTO.getWinnerId();
        if (parentContestDTO instanceof SingleContestDTO) {
            if (((SingleContestDTO) parentContestDTO).getPlayerOne().getId() == winnerId)
                return ((SingleContestDTO) parentContestDTO).getPlayerOne();
            if (((SingleContestDTO) parentContestDTO).getPlayerTwo().getId() == winnerId)
                return ((SingleContestDTO) parentContestDTO).getPlayerTwo();
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
        return SingleContestDTO.class;
    }

    @Override
    public Class<? extends Contest> getContestParticipantClass() {
        return SingleContest.class;
    }
}
