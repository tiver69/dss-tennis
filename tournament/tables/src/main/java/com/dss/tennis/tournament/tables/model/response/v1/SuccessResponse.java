package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<D> {

    private Set<ErrorData> warnings;
    private D data;
    private Set<ResourceObject> included;

    public SuccessResponse(D data, Set<ErrorData> warnings) {
        this.data = data;
        this.warnings = warnings;
    }
}
