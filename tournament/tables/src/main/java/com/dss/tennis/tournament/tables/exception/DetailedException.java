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
        this.errors.add(new DetailedErrorData(error, detailParameter));
    }

    public DetailedException(Set<DetailedErrorData> errors) {
        this.errors = errors;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailedErrorData {

        public DetailedErrorData(ErrorConstants errorConstant) {
            this.errorConstant = errorConstant;
        }

        private ErrorConstants errorConstant;
        private String detailParameter;
    }
}
