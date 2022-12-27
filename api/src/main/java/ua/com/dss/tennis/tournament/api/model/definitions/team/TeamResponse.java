package ua.com.dss.tennis.tournament.api.model.definitions.team;

import lombok.*;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;

import java.util.List;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TEAM;

@Data
@AllArgsConstructor
public class TeamResponse {

    private TeamResponseData data;
    private List<Object> included;

    public TeamResponse(TeamResponseData data) {
        this.data = data;
    }

    @Getter
    @Setter
    @Builder
    public static class TeamResponseData implements ua.com.dss.tennis.tournament.api.model.definitions.Data {
        private int id;
        private final String type = TEAM.value;
        private TeamResponseRelationships relationships;
        private Links links;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class TeamResponseRelationships {
        private SimpleResourceObject playerOne;
        private SimpleResourceObject playerTwo;
    }
}
