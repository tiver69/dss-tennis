package com.dss.tennis.tournament.tables.model.response.v1;

import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.StatusType;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
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
public class GetTournament {

    private Integer id;
    private String name;
    private TournamentType type;
    private ParticipantType participantType;
    private StatusType status;
    private LocalDate beginningDate;
    private List<GetContest> contests;
}
