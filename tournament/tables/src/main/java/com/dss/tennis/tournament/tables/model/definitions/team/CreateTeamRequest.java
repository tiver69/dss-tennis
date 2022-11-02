package com.dss.tennis.tournament.tables.model.definitions.team;

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
public class CreateTeamRequest {

    private final String type = ResourceObjectType.TEAM.value;
    private TeamRequestRelationships relationships;

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public class TeamRequestRelationships {
        private List<SimpleResourceObject> players;
    }
}
