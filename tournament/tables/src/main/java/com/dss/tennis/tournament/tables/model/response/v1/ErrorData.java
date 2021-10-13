package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorData {

    private String status;
    private String code;
    private String title;
    private String detail;

    private ErrorDataSource source;
    //todo: add additional links about for documentation
}
