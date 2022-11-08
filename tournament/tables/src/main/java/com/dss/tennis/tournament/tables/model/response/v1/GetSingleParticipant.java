package com.dss.tennis.tournament.tables.model.response.v1;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Deprecated
@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetSingleParticipant extends GetParticipant {

    {
        this.setType(ResourceObjectType.PLAYER.value);
    }

    public GetSingleParticipant(Integer id) {
        this.setId(id);
    }
}
