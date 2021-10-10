package com.dss.tennis.tournament.tables.exception;

import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class DetailedException extends RuntimeException {

    private Set<DetailedErrorData> errors = new HashSet<>();

    public DetailedException(ErrorConstants error) {
        this(error, null);
    }

    public DetailedException(ErrorConstants error, Object detailParameter) {
        this(error, detailParameter != null ? detailParameter.toString() : null);
    }

    public DetailedException(ErrorConstants error, String detailParameter) {
        this.errors.add(new DetailedErrorData(error, detailParameter, null));
    }

    public DetailedException(ErrorConstants error, String detailParameter, int sequentialNumber) {
        this.errors.add(new DetailedErrorData(error, detailParameter, sequentialNumber));
    }

    public DetailedException(Set<DetailedErrorData> errors) {
        this.errors = errors;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailedErrorData {

        public DetailedErrorData(String errorConstantString) {
            this.errorConstant = ErrorConstants.valueOf(errorConstantString);
        }

        public DetailedErrorData(ErrorConstants errorConstant) {
            this.errorConstant = errorConstant;
        }

        public DetailedErrorData(ErrorConstants errorConstant, String detailParameter) {
            this.errorConstant = errorConstant;
            this.detailParameter = detailParameter;
        }

        public DetailedErrorData(ErrorConstants errorConstant, Integer sequentNumber) {
            this.errorConstant = errorConstant;
            this.sequentNumber = sequentNumber;
        }

        private ErrorConstants errorConstant;
        private String detailParameter;
        private Integer sequentNumber;
    }
}
