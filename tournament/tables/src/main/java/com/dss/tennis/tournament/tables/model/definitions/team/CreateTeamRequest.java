package com.dss.tennis.tournament.tables.model.definitions.team;

import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamRelationships;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateTeamRequest {

    private final String type = ResourceObjectType.PLAYER.value;
    private TeamRelationships relationships;
}
