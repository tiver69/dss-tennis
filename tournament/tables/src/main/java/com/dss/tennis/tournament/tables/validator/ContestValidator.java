package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.ScoreHelper;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.*;

@Component
public class ContestValidator {

    @Autowired
    private ScoreHelper scoreHelper;
    @Autowired
    private ContestHelper contestHelper;
    @Autowired
    private ValidatorHelper<TechDefeatDTO> techDefeatDtoValidatorHelper;

    public void validateContestUpdateV1(Integer contestId, TournamentDTO tournamentDTO,
                                        boolean isContestWithoutSetScores) {
        if (TournamentType.ELIMINATION.equals(tournamentDTO.getTournamentType()))
            validateEliminationContestUpdateForbidden(contestId);
        if (isContestWithoutSetScores) throw new DetailedException(CONTEST_SCORE_NOT_FOUND);
    }

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

    public Set<ErrorDataDTO> validateTechDefeat(TechDefeatDTO techDefeatDto, Integer contestId,
                                                TournamentDTO tournamentDTO) {
        if (TournamentType.ELIMINATION.equals(tournamentDTO.getTournamentType())) {
            validateEliminationContestUpdateForbidden(contestId);
            if (techDefeatDto.getParticipantOne() && techDefeatDto.getParticipantTwo())
                throw new DetailedException(CONTEST_FULL_TECH_DEFEAT_FORBIDDEN);
        }
        return techDefeatDtoValidatorHelper.validateObject(techDefeatDto);
    }

    private void validateEliminationContestUpdateForbidden(ContestDTO contest) {
        if (contest.participantOneId() == null || contest.participantTwoId() == null) {
            throw new DetailedException(CONTEST_NOT_REACHED, contest.getId());
        }
        if (scoreHelper.getEliminationContestChildSetScores(contest.getId()).isScoreDefined() || contestHelper
                .isEliminationContestChildTechDefeat(contest.getId()))
            throw new DetailedException(CONTEST_SCORE_UPDATE_FORBIDDEN, contest.getId());
    }

    private void validateEliminationContestUpdateForbidden(Integer contestId) {
        if (scoreHelper.getEliminationContestChildSetScores(contestId).isScoreDefined() || contestHelper
                .isEliminationContestChildTechDefeat(contestId))
            throw new DetailedException(CONTEST_SCORE_UPDATE_FORBIDDEN, contestId);
    }
}
