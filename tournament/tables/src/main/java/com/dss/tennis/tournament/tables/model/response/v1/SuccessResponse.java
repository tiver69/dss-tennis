package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<D> {

    private D data;
    private List<ErrorData> warnings;
}
