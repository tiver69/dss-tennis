package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.db.v2.Score;
import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import com.dss.tennis.tournament.tables.repository.ScoreRepository;
import com.dss.tennis.tournament.tables.repository.SetScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class ScoreHelper {

    //    @Autowired
    private SetScoreRepository setScoreRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private ContestRepository contestRepository;

    public ScoreDTO getContestScoreDto(Integer contestId) {
        List<SetScore> sets = getContestSetScores(contestId);
        return mapSetScoreToDto(sets);
    }

    public List<SetScore> getContestSetScores(Integer contestId) {
        return setScoreRepository.findByContestId(contestId);
    }

    public List<SetScore> getContestScores(Integer contestId) {
        return setScoreRepository.findByContestId(contestId);
    }

    public boolean isEliminationContestChildScoreDefined(Integer contestId) {
        //todo better way
        Score participantOneScore = contestRepository
                .findChildContestParticipantOneScoreByEliminationContestId(contestId);
        Score participantTwoScore = contestRepository
                .findChildContestParticipantTwoScoreByEliminationContestId(contestId);
        return participantOneScore != null && participantTwoScore != null &&
                isScoreDefined(participantOneScore) && isScoreDefined(participantTwoScore);
    }

    public boolean isScoreDefined(Score score) {
        return score.getSetOne() + score.getSetTwo() + score.getSetThree() + score.getTieBreak() != 0 || score
                .getTechDefeat();
    }

    public ScoreDTO getEliminationContestChildSetScores(Integer contestId) {
        List<SetScore> setScores = setScoreRepository.findChildByEliminationContestId(contestId);
        return mapSetScoreToDto(setScores);
    }

    public ScoreDTO mapSetScoreToDto(List<SetScore> sets) {
        Map<SetType, SetScoreDTO> scoreSets = new HashMap<>();
        sets.forEach(set -> {
            scoreSets.put(set.getSetType(), new SetScoreDTO(set.getId(), set.getParticipantOne(), set
                    .getParticipantTwo()));
        });
        return new ScoreDTO(null, scoreSets.isEmpty() ? null : scoreSets);
    }

    public ScoreDTO mapSetScoreToDtoWithTechDefeatDetails(List<SetScore> sets, ContestDTO contest) {
        Map<SetType, SetScoreDTO> scoreSets = new HashMap<>();
        sets.forEach(set -> {
            scoreSets.put(set.getSetType(), new SetScoreDTO(set.getId(), set.getParticipantOne(), set
                    .getParticipantTwo()));
        });
        TechDefeatDTO techDefeatDTO = new TechDefeatDTO(contest.isParticipantOneTechDefeat(), contest
                .isParticipantTwoTechDefeat());
        return new ScoreDTO(techDefeatDTO, scoreSets);
    }

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

    public void createEmptyContestScore(Integer contestId) {
        SetScore setOne = mapEmptySetScore(SetType.SET_ONE, contestId);
        SetScore setTwo = mapEmptySetScore(SetType.SET_TWO, contestId);
        SetScore setThree = mapEmptySetScore(SetType.SET_THREE, contestId);
        SetScore tieBreak = mapEmptySetScore(SetType.TIE_BREAK, contestId);
        setScoreRepository.save(setOne);
        setScoreRepository.save(setTwo);
        setScoreRepository.save(setThree);
        setScoreRepository.save(tieBreak);
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

    public Function<ContestDTO, Integer> getScoreWinnerIdFunction(Map<SetType, ? extends SetScoreDTO> setScores) {
        byte result = 0;
        for (SetType key : setScores.keySet()) {
            if (key == SetType.TIE_BREAK) continue;
            SetScoreDTO setScore = setScores.get(key);
            Byte participantOneScore = setScore.getParticipantOneScore();
            Byte participantTwoScore = setScore.getParticipantTwoScore();

            if (participantOneScore == null && participantTwoScore == null) continue;
            if (participantOneScore.equals(participantTwoScore)) return (ContestDTO cc) -> null;
            if (isSetScoreComplete(participantOneScore, participantTwoScore)) {
                if (participantOneScore > participantTwoScore) result--;
                else result++;
            } else return (ContestDTO cc) -> null;
        }

        if (result == 0 || setScores.get(SetType.TIE_BREAK) != null)
            return getTieBreakSetWinner(setScores.get(SetType.TIE_BREAK));
        return result < 0 ? ContestDTO::participantOneId : ContestDTO::participantTwoId;
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
        return result < 0 ? ContestDTO::participantOneId : ContestDTO::participantTwoId;
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
        if (techDefeatDto.getParticipantOne()) return ContestDTO::participantTwoId;
        if (techDefeatDto.getParticipantTwo()) return ContestDTO::participantOneId;
        return (ContestDTO cc) -> null;
    }

    public void removeContestScore(Integer contestId) {
        setScoreRepository.removeByContestId(contestId);
    }

    private Function<ContestDTO, Integer> getTieBreakSetWinner(SetScoreDTO tieBreakSetScore) {
        if (tieBreakSetScore == null || tieBreakSetScore.getParticipantOneScore()
                .equals(tieBreakSetScore.getParticipantTwoScore()) || tieBreakSetScore.isSetScoreNotDefined())
            return (ContestDTO cc) -> null;

        return tieBreakSetScore.getParticipantOneScore() > tieBreakSetScore.getParticipantTwoScore() ?
                ContestDTO::participantOneId : ContestDTO::participantTwoId;
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

    private SetScore mapEmptySetScore(SetType setType, Integer contestId) {
        return SetScore.builder()
                .contestId(contestId)
                .participantOne((byte) 0)
                .participantTwo((byte) 0)
                .setType(setType)
                .build();
    }

    private boolean isSetScoreComplete(byte participantOne, byte participantTwo) {
        if ((participantOne == 6 && participantTwo <= 4) || (participantOne == 7 && (participantTwo == 6 || participantTwo == 5)))
            return true;
        return (participantOne <= 4 && participantTwo == 6) || ((participantOne == 6 || participantOne == 5) && participantTwo == 7);
    }
}

