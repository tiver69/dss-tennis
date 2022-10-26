package com.dss.tennis.tournament.tables.model.definitions.tournament;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class TournamentRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreteTournamentRequest {
        private final String type = ResourceObjectType.TOURNAMENT.value;
        private TournamentAttributes attributes;
    }

    @Getter
    @Setter
    public static class UpdateTournamentRequest extends CreteTournamentRequest {
        private int id;
    }
}
