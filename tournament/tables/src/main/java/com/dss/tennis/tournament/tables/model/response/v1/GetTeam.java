package com.dss.tennis.tournament.tables.model.response.v1;

import com.dss.tennis.tournament.tables.model.response.v1.GetTeam.GetTeamAttributes;
import lombok.Data;
import lombok.NoArgsConstructor;

@Deprecated
public class GetTeam extends ResourceObject<GetTeamAttributes> {

    {
        setType(ResourceObjectType.TEAM.value);
    }

    @Data
    @NoArgsConstructor
    public static class GetTeamAttributes {
        private GetPlayer playerOne;
        private GetPlayer playerTwo;
    }
}
