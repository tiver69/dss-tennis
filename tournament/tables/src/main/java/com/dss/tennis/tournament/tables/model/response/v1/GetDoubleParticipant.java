package com.dss.tennis.tournament.tables.model.response.v1;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetDoubleParticipant extends GetParticipant {

    {
        this.setType(ResourceObjectType.TEAM.value);
    }

    private Integer playerOneId;
    private Integer playerTwoId;
}
