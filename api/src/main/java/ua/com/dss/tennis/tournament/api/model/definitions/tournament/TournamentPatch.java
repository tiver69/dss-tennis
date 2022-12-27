package ua.com.dss.tennis.tournament.api.model.definitions.tournament;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.dss.tennis.tournament.api.model.db.v1.ParticipantType;
import ua.com.dss.tennis.tournament.api.model.db.v1.TournamentType;

import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPatch {

    private Optional<String> name;
    private Optional<TournamentType> tournamentType;
    private Optional<ParticipantType> participantType;
    private Optional<LocalDate> beginningDate;
}
