package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.PLAYER;

@Deprecated
@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetSingleParticipant extends GetParticipant {

    {
        this.setType(PLAYER.value);
    }

    public GetSingleParticipant(Integer id) {
        this.setId(id);
    }
}
