package com.dss.tennis.tournament.tables.model.definitions.tournament;

import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.StatusType;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
public class TournamentAttributes {

    private String name;
    private TournamentType tournamentType;
    private ParticipantType participantType;
    private StatusType status;
    private LocalDate beginningDate;
}
