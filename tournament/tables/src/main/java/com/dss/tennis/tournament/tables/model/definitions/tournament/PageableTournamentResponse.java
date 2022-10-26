package com.dss.tennis.tournament.tables.model.definitions.tournament;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageableTournamentResponse {
    private int totalPages;
    private List<TournamentResponseData> page;
    private Links links;
}
