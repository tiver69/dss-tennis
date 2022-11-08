package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.ScoreHelper;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ContestScorePatchDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.*;

@Component
public class ContestValidator {

    @Autowired
    private ScoreHelper scoreHelper;
    @Autowired
    private ContestHelper contestHelper;

    public void validateContestUpdate(ContestScorePatchDTO scorePatchDto, ContestDTO contest,
                                      TournamentDTO tournamentDTO) {
        if (TournamentType.ELIMINATION.equals(tournamentDTO.getTournamentType())) {
            validateEliminationContestUpdateForbidden(contest);
            validateFullTechDefeat(scorePatchDto.getTechDefeat(), contest.getId());
        }
    }

    public void validateFullTechDefeat(TechDefeatDTO techDefeatDto, Integer contestId) {
        if (techDefeatDto.getParticipantOne() && techDefeatDto.getParticipantTwo())
            throw new DetailedException(CONTEST_FULL_TECH_DEFEAT_FORBIDDEN, contestId);
    }

    private void validateEliminationContestUpdateForbidden(ContestDTO contest) {
        if (contest.participantOneId() == null || contest.participantTwoId() == null) {
            throw new DetailedException(CONTEST_NOT_REACHED, contest.getId());
        }
        if (scoreHelper.getEliminationContestChildSetScores(contest.getId()).isScoreDefined() || contestHelper
                .isEliminationContestChildTechDefeat(contest.getId()))
            throw new DetailedException(CONTEST_SCORE_UPDATE_FORBIDDEN, contest.getId());
    }
}
