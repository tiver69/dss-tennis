package com.dss.tennis.tournament.tables.model.request;

import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTournament {
    private String name;
    private TournamentType type;
    private List<CreatePlayer> players;
}
