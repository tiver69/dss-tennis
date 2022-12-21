package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.validator.constraint.anotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {

    public TournamentDTO(String name, TournamentType tournamentType, List<PlayerDTO> players) {
        this.name = name;
        this.tournamentType = tournamentType;
        this.players = players;
    }

    private int id;
    @Required(message = "TOURNAMENT_NAME_EMPTY")
    private String name;
    @Required(message = "TOURNAMENT_TYPE_EMPTY")
    private TournamentType tournamentType;
    @Required(message = "PARTICIPANT_TYPE_EMPTY")
    private ParticipantType participantType;
    private LocalDate beginningDate;
    private Iterable<ContestDTO> contests;
    private List<PlayerDTO> players;
}
