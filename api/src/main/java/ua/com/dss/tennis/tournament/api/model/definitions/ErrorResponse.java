package ua.com.dss.tennis.tournament.api.model.definitions;

import lombok.Data;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    public ErrorResponse(ErrorData errorData) {
        this.errors = Collections.singletonList(errorData);
    }

    private final List<ErrorData> errors;

    @lombok.Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorData {

        private String status;
        private String code;
        private String title;
        private String detail;

        private ErrorDataSource source;
        //todo: add additional links about for documentation

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ErrorDataSource {
            private String parameter;
            private String pointer;
        }
    }
}
