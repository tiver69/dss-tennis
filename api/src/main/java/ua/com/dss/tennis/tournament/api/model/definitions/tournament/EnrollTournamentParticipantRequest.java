package ua.com.dss.tennis.tournament.api.model.definitions.tournament;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;

import java.util.List;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TOURNAMENT;

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
