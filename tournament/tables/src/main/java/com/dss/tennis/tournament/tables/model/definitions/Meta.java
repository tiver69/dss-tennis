package com.dss.tennis.tournament.tables.model.definitions;

import com.dss.tennis.tournament.tables.model.response.v1.ErrorResponse.ErrorData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Set;

public class Meta {

    @Data
    @SuperBuilder
    @AllArgsConstructor
    public static class CommonMeta {
        private Set<ErrorData> warnings;
    }

    @Data
    @SuperBuilder
    public static class PageableMeta extends CommonMeta {
        private int totalPages;
        private int currentPage;
    }
}
