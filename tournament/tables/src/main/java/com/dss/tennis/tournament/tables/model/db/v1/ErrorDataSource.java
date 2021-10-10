package com.dss.tennis.tournament.tables.model.db.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDataSource {
    private String parameter;
    private String pointer;
}
