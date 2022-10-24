package com.dss.tennis.tournament.tables.model.definitions.team;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageableTeamResponse {
    private int totalPages;
    private List<TeamResponseData> page;
    private List<Object> included;
    private Links links;
}
