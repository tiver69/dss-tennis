package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TEAM;

@Deprecated
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetDoubleParticipant extends GetParticipant {

    {
        this.setType(TEAM.value);
    }

    private Integer playerOneId;
    private Integer playerTwoId;
}
