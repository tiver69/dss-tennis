package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.db.v2.Score;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import com.dss.tennis.tournament.tables.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ScoreHelper {

    @Autowired
    private ScoreRepository scoreRepository;

    public ScoreDTO applyPatch(ScoreDTO scoreDto, ScoreDTO patch) {
        applySetScorePatch(scoreDto.getSetOne(), patch.getSetOne());
        applySetScorePatch(scoreDto.getSetTwo(), patch.getSetTwo());
        applySetScorePatch(scoreDto.getSetThree(), patch.getSetThree());
        applySetScorePatch(scoreDto.getTieBreak(), patch.getTieBreak());

        if (patch.getTechDefeat() != null)
            scoreDto.setTechDefeat(patch.getTechDefeat());
        return scoreDto;
    }

    private void applySetScorePatch(SetScoreDTO setScoreDto, SetScoreDTO patch) {
        if (patch != null) {
            setScoreDto.setParticipantOneScore(patch.getParticipantOneScore());
            setScoreDto.setParticipantTwoScore(patch.getParticipantTwoScore());
        }
    }

    public void updateContestScore(ScoreDTO scoreDto) {
        scoreRepository.updateSetScoreById(scoreDto.getSetOne().getParticipantOneScore(),
                scoreDto.getSetTwo().getParticipantOneScore(),
                scoreDto.getSetThree().getParticipantOneScore(),
                scoreDto.getTieBreak().getParticipantOneScore(),
                scoreDto.getTechDefeat().getParticipantOne(),
                scoreDto.getParticipantOneScoreId());
        scoreRepository.updateSetScoreById(scoreDto.getSetOne().getParticipantTwoScore(),
                scoreDto.getSetTwo().getParticipantTwoScore(),
                scoreDto.getSetThree().getParticipantTwoScore(),
                scoreDto.getTieBreak().getParticipantTwoScore(),
                scoreDto.getTechDefeat().getParticipantTwo(),
                scoreDto.getParticipantTwoScoreId());
    }

    public void updateContestScoreTechDefeat(Integer scoreId) {
        scoreRepository.setTechDefeatByScoreById(scoreId);
    }

    public boolean isSetScoreValid(byte gameScore, byte otherGameScore) {
        boolean isSimpleScoreValid = (gameScore >= 0 && gameScore <= 6) && (otherGameScore >= 0 && otherGameScore <= 6);
        boolean isExtraSetScoreValid = (gameScore == 7 && (otherGameScore == 6 || otherGameScore == 5))
                || (otherGameScore == 7 && (gameScore == 6 || gameScore == 5));
        return isSimpleScoreValid || isExtraSetScoreValid;
    }

    public Function<ContestDTO, Integer> getWinnerIdFunctionByUpdatedScore(ScoreDTO scoreDto) {
        if (scoreDto.getTechDefeat().isTechDefeat()) {
            return getTechDefeatWinnerIdFunction(scoreDto.getTechDefeat());
        }

        byte result = 0;
        if (scoreDto.isSetOneScoreDefined()) {
            byte setOneCoefficient = getSetWinnerCoefficient(scoreDto.getSetOne());
            if (setOneCoefficient == 0) return (ContestDTO cc) -> null;
            else result += setOneCoefficient;
        }
        if (scoreDto.isSetTwoScoreDefined()) {
            byte setTwoCoefficient = getSetWinnerCoefficient(scoreDto.getSetTwo());
            if (setTwoCoefficient == 0) return (ContestDTO cc) -> null;
            else result += setTwoCoefficient;
        }
        if (scoreDto.isSetThreeScoreDefined()) {
            byte setThreeCoefficient = getSetWinnerCoefficient(scoreDto.getSetThree());
            if (setThreeCoefficient == 0) return (ContestDTO cc) -> null;
            else result += setThreeCoefficient;
        }

        if (result == 0)
            return getTieBreakSetWinner(scoreDto.getTieBreak());
        return result < 0 ? ContestDTO::getParticipantOneId : ContestDTO::getParticipantTwoId;
    }

    public byte getSetWinnerCoefficient(SetScoreDTO setScore) {
        Byte participantOneScore = setScore.getParticipantOneScore();
        Byte participantTwoScore = setScore.getParticipantTwoScore();

        if (participantOneScore.equals(participantTwoScore)) return (byte) 0;
        if (isSetScoreComplete(participantOneScore, participantTwoScore)) {
            return participantOneScore > participantTwoScore ? (byte) -1 : 1;
        } else return (byte) 0;
    }

    public Function<ContestDTO, Integer> getTechDefeatWinnerIdFunction(TechDefeatDTO techDefeatDto) {
        if (techDefeatDto.getParticipantOne() && techDefeatDto.getParticipantTwo()) return (ContestDTO cc) -> null;
        if (techDefeatDto.getParticipantOne()) return ContestDTO::getParticipantTwoId;
        if (techDefeatDto.getParticipantTwo()) return ContestDTO::getParticipantOneId;
        return (ContestDTO cc) -> null;
    }

    private Function<ContestDTO, Integer> getTieBreakSetWinner(SetScoreDTO tieBreakSetScore) {
        if (tieBreakSetScore == null || tieBreakSetScore.getParticipantOneScore()
                .equals(tieBreakSetScore.getParticipantTwoScore()) || tieBreakSetScore.isSetScoreNotDefined())
            return (ContestDTO cc) -> null;

        return tieBreakSetScore.getParticipantOneScore() > tieBreakSetScore.getParticipantTwoScore() ?
                ContestDTO::getParticipantOneId : ContestDTO::getParticipantTwoId;
    }

    public Score mapEmptyParticipantScore() {
        return Score.builder()
                .setOne((byte) 0)
                .setTwo((byte) 0)
                .setThree((byte) 0)
                .tieBreak((byte) 0)
                .techDefeat(false)
                .build();
    }

    private boolean isSetScoreComplete(byte participantOne, byte participantTwo) {
        if ((participantOne == 6 && participantTwo <= 4) || (participantOne == 7 && (participantTwo == 6 || participantTwo == 5)))
            return true;
        return (participantOne <= 4 && participantTwo == 6) || ((participantOne == 6 || participantOne == 5) && participantTwo == 7);
    }
}

