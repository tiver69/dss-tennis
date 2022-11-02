package com.dss.tennis.tournament.tables.model.definitions.team;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.*;

import java.util.List;

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
    public static class TeamResponseData {
        private int id;
        private final String type = ResourceObjectType.TEAM.value;
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
