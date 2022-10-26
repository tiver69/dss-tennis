package com.dss.tennis.tournament.tables.model.definitions.tournament;

import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EnrollTournamentParticipantRequest {

    private int id;
    private final String type = ResourceObjectType.TOURNAMENT.value;
    private EnrollTournamentParticipantRelationships relationships;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnrollTournamentParticipantRelationships {
        List<SimpleResourceObject> participants;
    }
}
