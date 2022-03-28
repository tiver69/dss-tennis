package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.helper.ScoreHelper;
import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScorePatchDTO;
import com.dss.tennis.tournament.tables.model.dto.ScorePatchDTO.SetScorePatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.*;
import static com.dss.tennis.tournament.tables.model.db.v2.SetType.SET_ONE;
import static com.dss.tennis.tournament.tables.model.db.v2.SetType.SET_TWO;

@Component
public class ScoreValidator {

    @Autowired
    private ScoreHelper scoreHelper;
    @Autowired
    private ValidatorHelper<SetScoreDTO> setScoreValidatorHelper;

    public Set<ErrorDataDTO> validateUpdateScorePatch(ScoreDTO scoreDto, ScorePatchDTO patch) {
        Set<ErrorDataDTO> errors = new HashSet<>();

        patch.getSets().keySet().forEach(setType -> {
            SetScoreDTO score = scoreDto.getSets().get(setType);
            SetScorePatchDTO patchScore = patch.getSets().get(setType);

            if ((score != null && !score.getId().equals(patchScore.getId())) || (score == null && !patchScore
                    .isCreatePatch())) {
                errors.add(ErrorDataDTO.builder().errorConstant(SET_SCORE_NOT_FOUND).pointer(setType.value).build());
            }
        });
        return errors;
    }

    public Set<ErrorDataDTO> validateCreateScore(ScoreDTO score) {
        Set<ErrorDataDTO> errors = new HashSet<>();
        Map<SetType, SetScoreDTO> sets = score.getSets();
        sets.keySet().stream().map(key -> validateSetScoreCreate(sets.get(key), key.value))
                .filter(Objects::nonNull).forEach(errors::addAll);

        if (sets.get(SET_ONE) == null)
            errors.add(ErrorDataDTO.builder().errorConstant(SET_SCORE_EMPTY).pointer(SET_ONE.value).build());
        else if (sets.get(SetType.SET_THREE) != null && sets.get(SET_TWO) == null)
            errors.add(ErrorDataDTO.builder().errorConstant(SET_SCORE_EMPTY).pointer(SET_TWO.value).build());
        return errors;
    }

    public Set<ErrorDataDTO> validateUpdateScore(ScorePatchDTO score) {
        Set<ErrorDataDTO> errors = new HashSet<>();
        Map<SetType, SetScorePatchDTO> sets = score.getSets();
        sets.keySet().stream().map(key -> validateSetScorePatch(sets.get(key), key.value)).filter(Objects::nonNull)
                .forEach(errors::addAll);

        if (isSetScorePatchDelete(sets.get(SET_ONE)))
            errors.add(ErrorDataDTO.builder().errorConstant(SET_SCORE_EMPTY).pointer(SET_ONE.value).build());
        else if (!isSetScorePatchDelete(sets.get(SetType.SET_THREE)) && isSetScorePatchDelete(sets.get(SET_TWO)))
            errors.add(ErrorDataDTO.builder().errorConstant(SET_SCORE_EMPTY).pointer(SET_TWO.value).build());
        return errors;
    }

    private Set<ErrorDataDTO> validateSetScoreCreate(SetScoreDTO setScore, String type) {
        Set<ErrorDataDTO> errorDataDTOs = setScoreValidatorHelper.validateObject(setScore, type);

        return errorDataDTOs.isEmpty() ? validateSetScoreLimit(setScore
                .getParticipantOneScore(), setScore.getParticipantTwoScore(), type) : errorDataDTOs;
    }

    private Set<ErrorDataDTO> validateSetScorePatch(SetScorePatchDTO setScorePatch, String type) {
        Set<ErrorDataDTO> errorDataDTOs = setScorePatch.isCreatePatch() ? setScoreValidatorHelper
                .validateObject(setScorePatch, type) : new HashSet<>();

        return errorDataDTOs.isEmpty() ? validateSetScoreLimit(setScorePatch
                .getParticipantOneScore(), setScorePatch.getParticipantTwoScore(), type) : errorDataDTOs;
    }

    private Set<ErrorDataDTO> validateSetScoreLimit(Byte participantOne, Byte participantTwo, String type) {
        if (participantOne != null && participantTwo != null && !scoreHelper
                .isSetScoreValid(participantOne, participantTwo))
            return Set.of(ErrorDataDTO.builder().errorConstant(GAME_LIMIT_EXCEEDED).pointer(type)
                    .detailParameter(String.format("%d:%d", participantOne, participantTwo)).build());
        return null;
    }

    private boolean isSetScorePatchDelete(SetScorePatchDTO scorePatch) {
        return scorePatch == null || scorePatch.isDeletePatch();
    }
}
