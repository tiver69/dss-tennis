package com.dss.tennis.tournament.tables.model.definitions.player;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageablePlayerResponse {
    private int totalPages;
    private List<PlayerResponseData> page;
    private Links links;
}
