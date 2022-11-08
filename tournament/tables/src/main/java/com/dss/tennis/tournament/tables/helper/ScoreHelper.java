package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ContestScorePatchDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import com.dss.tennis.tournament.tables.repository.SetScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class ScoreHelper {

    @Autowired
    private SetScoreRepository scoreRepository;

    public ScoreDTO getContestScoreDto(Integer contestId) {
        List<SetScore> sets = getContestSetScores(contestId);
        return mapSetScoreToDto(sets);
    }

    public List<SetScore> getContestSetScores(Integer contestId) {
        return scoreRepository.findByContestId(contestId);
    }

    public ScoreDTO getEliminationContestChildSetScores(Integer contestId) {
        List<SetScore> setScores = scoreRepository.findChildByEliminationContestId(contestId);
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

    public ScoreDTO applyPatch(ScoreDTO scoreDto, ContestScorePatchDTO patch) {
        if (patch.getSets() != null)
            scoreDto.getSets().keySet().forEach(setType -> {
                Integer currentSetScoreId = scoreDto.getSets().get(setType).getId();
                SetScoreDTO setScoreFromPatch = patch.getSets().get(setType);
                if (setScoreFromPatch != null)
                    scoreDto.getSets()
                            .put(setType, new SetScoreDTO(currentSetScoreId, setScoreFromPatch
                                    .getParticipantOneScore(), setScoreFromPatch
                                    .getParticipantTwoScore()));
            });

        if (patch.getTechDefeat() != null)
            scoreDto.setTechDefeat(new TechDefeatDTO(patch.getTechDefeat().getParticipantOne(), patch.getTechDefeat()
                    .getParticipantTwo()));

        return scoreDto;
    }

    public void createEmptyContestScore(Integer contestId) {
        SetScore setOne = mapEmptySetScore(SetType.SET_ONE, contestId);
        SetScore setTwo = mapEmptySetScore(SetType.SET_TWO, contestId);
        SetScore setThree = mapEmptySetScore(SetType.SET_THREE, contestId);
        SetScore tieBreak = mapEmptySetScore(SetType.TIE_BREAK, contestId);
        scoreRepository.save(setOne);
        scoreRepository.save(setTwo);
        scoreRepository.save(setThree);
        scoreRepository.save(tieBreak);
    }

    public void updateContestScore(ScoreDTO scoreDto) {
        Map<SetType, SetScoreDTO> sets = scoreDto.getSets();
        sets.keySet().forEach(setType -> {
            SetScoreDTO setScoreDto = scoreDto.getSets().get(setType);
            scoreRepository
                    .updateSetScoreById(setScoreDto.getParticipantOneScore(), setScoreDto
                            .getParticipantTwoScore(), setScoreDto.getId());
        });
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

    public Function<ContestDTO, Integer> getScoreWinnerIdFunctionWithCreatedScore(Map<SetType, ? extends SetScoreDTO> setScores) {
        byte result = 0;
        for (SetType key : setScores.keySet()) {
            SetScoreDTO setScore = setScores.get(key);
            if (key == SetType.TIE_BREAK || setScore.isSetScoreNotDefined()) continue;

            Byte participantOneScore = setScore.getParticipantOneScore();
            Byte participantTwoScore = setScore.getParticipantTwoScore();

            if (participantOneScore.equals(participantTwoScore)) return (ContestDTO cc) -> null;
            if (isSetScoreComplete(participantOneScore, participantTwoScore)) {
                if (participantOneScore > participantTwoScore) result--;
                else result++;
            } else return (ContestDTO cc) -> null;
        }

        if (result == 0)
            return getTieBreakSetWinner(setScores.get(SetType.TIE_BREAK));
        return result < 0 ? ContestDTO::participantOneId : ContestDTO::participantTwoId;
    }

    public Function<ContestDTO, Integer> getTechDefeatWinnerIdFunction(TechDefeatDTO techDefeatDto) {
        if (techDefeatDto.getParticipantOne() && techDefeatDto.getParticipantTwo()) return (ContestDTO cc) -> null;
        if (techDefeatDto.getParticipantOne()) return ContestDTO::participantTwoId;
        if (techDefeatDto.getParticipantTwo()) return ContestDTO::participantOneId;
        return (ContestDTO cc) -> null;
    }

    public void removeContestScore(Integer contestId) {
        scoreRepository.removeByContestId(contestId);
    }

    private Function<ContestDTO, Integer> getTieBreakSetWinner(SetScoreDTO tieBreakSetScore) {
        if (tieBreakSetScore == null || tieBreakSetScore.getParticipantOneScore()
                .equals(tieBreakSetScore.getParticipantTwoScore()) || tieBreakSetScore.isSetScoreNotDefined())
            return (ContestDTO cc) -> null;

        return tieBreakSetScore.getParticipantOneScore() > tieBreakSetScore.getParticipantTwoScore() ?
                ContestDTO::participantOneId : ContestDTO::participantTwoId;
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

