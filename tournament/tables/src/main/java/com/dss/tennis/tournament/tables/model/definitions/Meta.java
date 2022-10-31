package com.dss.tennis.tournament.tables.model.definitions;

import com.dss.tennis.tournament.tables.model.response.v1.ErrorResponse.ErrorData;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Meta {

    private int totalPages;
    private int currentPage;
    private Set<ErrorData> warnings;
}
