package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.validator.anotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {

    public TournamentDTO(String name, TournamentType type, List<PlayerDTO> players) {
        this.name = name;
        this.type = type;
        this.players = players;
    }

    private int id;
    @Required(message = "TOURNAMENT_NAME_EMPTY")
    private String name;
    private TournamentType type;
    private List<ContestDTO> contests;
    private List<PlayerDTO> players;
}
