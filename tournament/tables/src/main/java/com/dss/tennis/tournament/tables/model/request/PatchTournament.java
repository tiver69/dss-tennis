package com.dss.tennis.tournament.tables.model.request;

import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchTournament {

    private Optional<String> name;
    private Optional<TournamentType> tournamentType;
    private Optional<ParticipantType> participantType;
    private Optional<LocalDate> beginningDate;
}
