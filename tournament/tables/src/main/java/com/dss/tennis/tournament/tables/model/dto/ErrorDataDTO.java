package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDataDTO {

    public ErrorDataDTO(String errorConstantString) {
        this.errorConstant = ErrorConstants.valueOf(errorConstantString);
    }

    public ErrorDataDTO(ErrorConstants errorConstant) {
        this.errorConstant = errorConstant;
    }

    public ErrorDataDTO(ErrorConstants errorConstant, String detailParameter) {
        this.errorConstant = errorConstant;
        this.detailParameter = detailParameter;
    }

    public ErrorDataDTO(ErrorConstants errorConstant, Byte sequentNumber) {
        this.errorConstant = errorConstant;
        this.sequentNumber = sequentNumber;
    }

    public ErrorDataDTO(ErrorConstants errorConstant, String detailParameter, Byte sequentNumber) {
        this.errorConstant = errorConstant;
        this.detailParameter = detailParameter;
        this.sequentNumber = sequentNumber;
    }

    private ErrorConstants errorConstant;
    private String detailParameter;
    private String pointer;
    private Byte sequentNumber;
}
