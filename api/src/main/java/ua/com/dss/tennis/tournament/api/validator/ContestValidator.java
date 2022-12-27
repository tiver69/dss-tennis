package ua.com.dss.tennis.tournament.api.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.helper.ContestHelper;
import ua.com.dss.tennis.tournament.api.model.db.v1.TournamentType;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ScoreDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TechDefeatDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.*;

@Component
public class ContestValidator {

    @Autowired
    private ContestHelper contestHelper;

    public void validateContestUpdate(ScoreDTO scorePatchDto, ContestDTO contest, TournamentDTO tournamentDTO) {
        if (TournamentType.ELIMINATION.equals(tournamentDTO.getTournamentType())) {
            validateEliminationContestUpdateForbidden(contest);
            validateFullTechDefeat(scorePatchDto.getTechDefeat(), contest.getId());
        }
    }

    public void validateFullTechDefeat(TechDefeatDTO techDefeatDto, Integer contestId) {
        if (techDefeatDto != null && techDefeatDto.getParticipantOne() && techDefeatDto.getParticipantTwo())
            throw new DetailedException(CONTEST_FULL_TECH_DEFEAT_FORBIDDEN, contestId);
    }

    private void validateEliminationContestUpdateForbidden(ContestDTO contest) {
        if (contest.getParticipantOneId() == null || contest.getParticipantTwoId() == null) {
            throw new DetailedException(CONTEST_NOT_REACHED, contest.getId());
        }
        if (contestHelper.isEliminationContestChildScoreDefined(contest.getId()))
            throw new DetailedException(CONTEST_SCORE_UPDATE_FORBIDDEN, contest.getId());
    }
}
