package com.dss.tennis.tournament.tables.model.response.v1;

import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTournament {

    private Integer id;
    private String name;
    private TournamentType type;
    private List<GetContest> contests;
}
