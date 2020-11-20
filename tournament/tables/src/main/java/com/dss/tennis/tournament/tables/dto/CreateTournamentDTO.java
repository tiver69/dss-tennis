package com.dss.tennis.tournament.tables.dto;

import com.dss.tennis.tournament.tables.model.v1.TournamentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTournamentDTO {

    private String name;
    private TournamentType type;
    private List<String> players;

}
