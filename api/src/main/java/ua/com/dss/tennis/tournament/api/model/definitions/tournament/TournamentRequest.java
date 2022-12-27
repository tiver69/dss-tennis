package ua.com.dss.tennis.tournament.api.model.definitions.tournament;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TOURNAMENT;

public class TournamentRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreteTournamentRequest {
        private final String type = TOURNAMENT.value;
        private TournamentAttributes attributes;
    }

    @Getter
    @Setter
    public static class UpdateTournamentRequest extends CreteTournamentRequest {
        private int id;
    }
}
