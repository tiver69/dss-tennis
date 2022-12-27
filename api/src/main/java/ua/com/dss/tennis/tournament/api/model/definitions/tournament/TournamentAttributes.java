package ua.com.dss.tennis.tournament.api.model.definitions.tournament;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.com.dss.tennis.tournament.api.model.db.v1.ParticipantType;
import ua.com.dss.tennis.tournament.api.model.db.v1.TournamentType;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
public class TournamentAttributes {

    private String name;
    private TournamentType tournamentType;
    private ParticipantType participantType;
    private LocalDate beginningDate;
}
