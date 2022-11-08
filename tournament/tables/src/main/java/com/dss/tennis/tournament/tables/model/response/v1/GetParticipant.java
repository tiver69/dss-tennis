package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Deprecated
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class GetParticipant {

    private Integer id;
    private String type;
}
