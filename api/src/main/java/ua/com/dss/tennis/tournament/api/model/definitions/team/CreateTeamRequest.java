package ua.com.dss.tennis.tournament.api.model.definitions.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;

import java.util.List;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TEAM;

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
