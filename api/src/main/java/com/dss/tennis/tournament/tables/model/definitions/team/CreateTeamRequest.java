package com.dss.tennis.tournament.tables.model.definitions.team;

import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TEAM;

@Getter
@Setter
@NoArgsConstructor
public class CreateTeamRequest {

    private final String type = TEAM.value;
    private TeamRequestRelationships relationships;

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public class TeamRequestRelationships {
        private List<SimpleResourceObject> players;
    }
}
