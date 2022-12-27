package ua.com.dss.tennis.tournament.api.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.helper.ScoreHelper;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ScoreDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ScoreDTO.SetScoreDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TechDefeatDTO;

import java.util.HashSet;
import java.util.Set;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.*;
import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorPointerKey.*;

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
        errors.addAll(validateTieBreakScorePatch(scorePatchDto.getTieBreak()));
        return errors;
    }

    public Set<ErrorDataDTO> validateUpdateScore(ScoreDTO score) {
        Set<ErrorDataDTO> errors = new HashSet<>();
        if (score.isSetOneScoreNotDefined() && score.isSetTwoScoreDefined())
            errors.add(ErrorDataDTO.builder().errorKey(SET_SCORE_EMPTY).pointer(SET_ONE.value).build());
        if (score.isSetTwoScoreNotDefined() && score.isSetThreeScoreDefined())
            errors.add(ErrorDataDTO.builder().errorKey(SET_SCORE_EMPTY).pointer(SET_TWO.value).build());
        if (score.isTieBreakScoreDefined() && score.isSetOneScoreNotDefined())
            errors.add(ErrorDataDTO.builder().errorKey(SET_SCORE_EMPTY).pointer(TIE_BREAK.value).build());

        return errors;
    }

    private Set<ErrorDataDTO> validateSetScorePatch(SetScoreDTO setScoreDTO, String type) {
        if (setScoreDTO == null) return Set.of();
        Set<ErrorDataDTO> errorDataDTOs = setScoreValidatorHelper.validateObject(setScoreDTO, type);

        return errorDataDTOs.isEmpty() ? validateSetScoreLimit(setScoreDTO
                .getParticipantOneScore(), setScoreDTO.getParticipantTwoScore(), type) : errorDataDTOs;
    }

    private Set<ErrorDataDTO> validateTieBreakScorePatch(SetScoreDTO setScoreDTO) {
        if (setScoreDTO == null) return Set.of();
        Set<ErrorDataDTO> errorDataDTOs = setScoreValidatorHelper.validateObject(setScoreDTO, TIE_BREAK.value);

        return errorDataDTOs.isEmpty() ? validateTieBreakScoreLimit(setScoreDTO.getParticipantOneScore(), setScoreDTO
                .getParticipantTwoScore()) : errorDataDTOs;
    }

    private Set<ErrorDataDTO> validateSetScoreLimit(Byte participantOne, Byte participantTwo, String type) {
        if (participantOne != null && participantTwo != null && !scoreHelper
                .isSetScoreValid(participantOne, participantTwo))
            return Set.of(ErrorDataDTO.builder().errorKey(GAME_LIMIT_EXCEEDED).pointer(type)
                    .detailParameter(String.format("%d:%d", participantOne, participantTwo)).build());
        return new HashSet<>();
    }

    private Set<ErrorDataDTO> validateTieBreakScoreLimit(Byte participantOne, Byte participantTwo) {
        if (participantOne != null && participantTwo != null && !scoreHelper
                .isTieBreakScoreValid(participantOne, participantTwo))
            return Set.of(ErrorDataDTO.builder().errorKey(GAME_LIMIT_EXCEEDED).pointer(TIE_BREAK.value)
                    .detailParameter(String.format("%d:%d", participantOne, participantTwo)).build());
        return new HashSet<>();
    }
}
