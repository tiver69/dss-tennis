package ua.com.dss.tennis.tournament.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDataDTO {

    public ErrorDataDTO(String errorConstantString) {
        this.errorKey = ErrorKey.valueOf(errorConstantString);
    }

    public ErrorDataDTO(ErrorKey errorKey) {
        this.errorKey = errorKey;
    }

    public ErrorDataDTO(ErrorKey errorKey, String detailParameter) {
        this.errorKey = errorKey;
        this.detailParameter = detailParameter;
    }

    public ErrorDataDTO(ErrorKey errorKey, Byte sequentNumber) {
        this.errorKey = errorKey;
        this.sequentNumber = sequentNumber;
    }

    public ErrorDataDTO(ErrorKey errorKey, String detailParameter, Byte sequentNumber) {
        this.errorKey = errorKey;
        this.detailParameter = detailParameter;
        this.sequentNumber = sequentNumber;
    }

    private ErrorKey errorKey;
    private String detailParameter;
    private String pointer;
    private Byte sequentNumber;
}
