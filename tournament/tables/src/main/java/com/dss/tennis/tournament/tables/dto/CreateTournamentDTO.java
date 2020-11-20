package com.dss.tennis.tournament.tables.dto;

import com.dss.tennis.tournament.tables.model.v1.TournamentType;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CreateTournamentDTO {

    private String name;
    private TournamentType type;
    private ArrayList<String> players;

}
