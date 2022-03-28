package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScorePatchDTO;
import com.dss.tennis.tournament.tables.model.dto.ScorePatchDTO.SetScorePatchDTO;
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

    public ScoreDTO mapSetScoreToDto(List<SetScore> sets) {
        Map<SetType, SetScoreDTO> scoreSets = new HashMap<>();
        sets.forEach(set -> {
            scoreSets.put(set.getSetType(), new SetScoreDTO(set.getId(), set.getParticipantOne(), set
                    .getParticipantTwo()));
        });
        return new ScoreDTO(scoreSets);
    }

    public ScorePatchDTO applyPatch(ScoreDTO scoreDto, ScorePatchDTO patch) {
        Map<SetType, SetScorePatchDTO> scoreSetPatches = new HashMap<>();
        scoreDto.getSets().keySet().forEach(setType -> scoreSetPatches
                .put(setType, new SetScorePatchDTO(scoreDto.getSets().get(setType))));

        patch.getSets().keySet().forEach(setType -> {
            SetScoreDTO score = scoreDto.getSets().get(setType);
            SetScorePatchDTO patchScore = patch.getSets().get(setType);
            if (score != null && !patchScore.isDeletePatch()) {
                if (patchScore.getParticipantOneScore() == null)
                    patchScore.setParticipantOneScore(score.getParticipantOneScore());
                if (patchScore.getParticipantTwoScore() == null)
                    patchScore.setParticipantTwoScore(score.getParticipantTwoScore());
            }
            scoreSetPatches.put(setType, patchScore);
        });

        return new ScorePatchDTO(scoreSetPatches);
    }

    public void createContestScore(ScoreDTO scoreDto, Integer contestId) {
        scoreDto.getSets().keySet().forEach(setType -> {
            SetScore score = mapSetScore(scoreDto.getSets().get(setType), setType, contestId);
            scoreRepository.save(score);
        });
    }

    public void updateContestScore(ScorePatchDTO scoreDto, Integer contestId) {
        scoreDto.getSets().keySet().forEach(setType -> {
            SetScorePatchDTO setScoreDto = scoreDto.getSets().get(setType);
            if (setScoreDto.isCreatePatch())
                scoreRepository.save(mapSetScore(setScoreDto, setType, contestId));
            else if (setScoreDto.isDeletePatch())
                scoreRepository.removeById(setScoreDto.getId());
            else
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

    public Function<ContestDTO, Integer> getWinnerIdFunction(Map<SetType, ? extends SetScoreDTO> setScores) {
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

    private Function<ContestDTO, Integer> getTieBreakSetWinner(SetScoreDTO tieBreakSetScore) {
        if (tieBreakSetScore == null || tieBreakSetScore.getParticipantOneScore()
                .equals(tieBreakSetScore.getParticipantTwoScore()))
            return (ContestDTO cc) -> null;

        return tieBreakSetScore.getParticipantOneScore() > tieBreakSetScore.getParticipantTwoScore() ?
                ContestDTO::participantOneId : ContestDTO::participantTwoId;
    }

    private SetScore mapSetScore(SetScoreDTO setScore, SetType setType, Integer contestId) {
        return SetScore.builder()
                .contest(Contest.builder().id(contestId).build())
                .participantOne(setScore.getParticipantOneScore())
                .participantTwo(setScore.getParticipantTwoScore())
                .setType(setType)
                .build();
    }

    private boolean isSetScoreComplete(byte participantOne, byte participantTwo) {
        if ((participantOne == 6 && participantTwo <= 4) || (participantOne == 7 && (participantTwo == 6 || participantTwo == 5)))
            return true;
        return (participantOne <= 4 && participantTwo == 6) || ((participantOne == 6 || participantOne == 5) && participantTwo == 7);
    }
}

