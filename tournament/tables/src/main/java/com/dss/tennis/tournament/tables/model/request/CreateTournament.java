package com.dss.tennis.tournament.tables.model.request;

import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTournament {
    private String name;
    private TournamentType tournamentType;
    private ParticipantType participantType;
    private LocalDate beginningDate;
}
