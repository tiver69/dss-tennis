package com.dss.tennis.tournament.tables.model.definitions.tournament;

import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TOURNAMENT;

@Getter
@Setter
@NoArgsConstructor
public class EnrollTournamentParticipantRequest {

    private int id;
    private final String type = TOURNAMENT.value;
    private EnrollTournamentParticipantRelationships relationships;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnrollTournamentParticipantRelationships {
        List<SimpleResourceObject> participants;
    }
}
