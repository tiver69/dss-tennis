package ua.com.dss.tennis.tournament.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.dss.tennis.tournament.api.model.db.v1.ParticipantType;
import ua.com.dss.tennis.tournament.api.model.db.v1.TournamentType;
import ua.com.dss.tennis.tournament.api.validator.constraint.anotation.Required;

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
