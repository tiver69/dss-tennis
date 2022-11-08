package com.dss.tennis.tournament.tables.model.response.v1;

import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.StatusType;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TOURNAMENT;

@Deprecated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTournament<T extends GetParticipant> {

    {
        this.setType(TOURNAMENT.value);
    }

    private Integer id;
    private String type;
    private String name;
    private TournamentType tournamentType;
    private ParticipantType participantType;
    private StatusType status;
    private LocalDate beginningDate;
    private Iterable<T> contests;
}
