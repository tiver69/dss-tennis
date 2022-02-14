package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<D> {

    private D data;
    private Set<ResourceObject> included;
    private List<ErrorData> warnings;

    public SuccessResponse(D data, List<ErrorData> warnings) {
        this.data = data;
        this.warnings = warnings;
    }
}
