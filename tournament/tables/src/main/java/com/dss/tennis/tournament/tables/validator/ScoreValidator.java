package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.helper.ScoreHelper;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.ErrorKey.*;
import static com.dss.tennis.tournament.tables.exception.ErrorConstants.ErrorPointerKey.*;

@Component
public class ScoreValidator {

    @Autowired
    private ScoreHelper scoreHelper;
    @Autowired
    private ValidatorHelper<SetScoreDTO> setScoreValidatorHelper;
    @Autowired
    private ValidatorHelper<TechDefeatDTO> techDefeatDtoValidatorHelper;

    public Set<ErrorDataDTO> validateUpdateScorePatch(ScoreDTO scorePatchDto) {
        Set<ErrorDataDTO> errors = new HashSet<>();

        if (scorePatchDto.getTechDefeat() == null && scorePatchDto.isScoreNotDefined()) {
            errors.add(ErrorDataDTO.builder().errorKey(CONTEST_UPDATE_ATTRIBUTES_EMPTY).build());
            return errors;
        }

        if (scorePatchDto.getTechDefeat() != null) {
            errors = techDefeatDtoValidatorHelper.validateObject(scorePatchDto.getTechDefeat());
        }
        errors.addAll(validateSetScorePatch(scorePatchDto.getSetOne(), SET_ONE.value));
        errors.addAll(validateSetScorePatch(scorePatchDto.getSetTwo(), SET_TWO.value));
        errors.addAll(validateSetScorePatch(scorePatchDto.getSetThree(), SET_THREE.value));
        errors.addAll(validateSetScorePatch(scorePatchDto.getTieBreak(), TIE_BREAK.value));
        return errors;
    }

    public Set<ErrorDataDTO> validateUpdateScore(ScoreDTO score) {
        Set<ErrorDataDTO> errors = new HashSet<>();
        //todo define rule for tie break
        if (score.isSetOneScoreNotDefined() && score.isSetTwoScoreDefined())
            errors.add(ErrorDataDTO.builder().errorKey(SET_SCORE_EMPTY).pointer(SET_ONE.value).build());
        if (score.isSetTwoScoreNotDefined() && score.isSetThreeScoreDefined())
            errors.add(ErrorDataDTO.builder().errorKey(SET_SCORE_EMPTY).pointer(SET_TWO.value).build());
        return errors;
    }

    private Set<ErrorDataDTO> validateSetScorePatch(SetScoreDTO setScoreDTO, String type) {
        if (setScoreDTO == null) return Set.of();
        Set<ErrorDataDTO> errorDataDTOs = setScoreValidatorHelper.validateObject(setScoreDTO, type);

        return errorDataDTOs.isEmpty() ? validateSetScoreLimit(setScoreDTO
                .getParticipantOneScore(), setScoreDTO.getParticipantTwoScore(), type) : errorDataDTOs;
    }

    private Set<ErrorDataDTO> validateSetScoreLimit(Byte participantOne, Byte participantTwo, String type) {
        //todo determine rules for tie break score
        if (participantOne != null && participantTwo != null && !scoreHelper
                .isSetScoreValid(participantOne, participantTwo))
            return Set.of(ErrorDataDTO.builder().errorKey(GAME_LIMIT_EXCEEDED).pointer(type)
                    .detailParameter(String.format("%d:%d", participantOne, participantTwo)).build());
        return new HashSet<>();
    }
}
