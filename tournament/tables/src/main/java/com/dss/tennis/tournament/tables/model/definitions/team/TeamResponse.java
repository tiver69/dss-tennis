package com.dss.tennis.tournament.tables.model.definitions.team;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.*;

import java.util.List;

@Data
public class TeamResponse {

    private TeamResponseData data;

    public TeamResponse(TeamResponseData data) {
        this.data = data;
    }

    @Getter
    @Setter
    @Builder
    public static class TeamResponseData {
        private int id;
        private final String type = ResourceObjectType.TEAM.value;
        private TeamRelationships relationships;
        private List<Object> included;
        private Links links;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class TeamRelationships {
        private List<SimpleResourceObject> players;
    }
}
