package ua.com.dss.tennis.tournament.api.model.definitions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;

import java.util.Set;

public class Meta {

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommonMeta {
        private Set<ErrorData> warnings;
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class PageableMeta extends CommonMeta {
        private int totalPages;
        private int currentPage;
    }
}
