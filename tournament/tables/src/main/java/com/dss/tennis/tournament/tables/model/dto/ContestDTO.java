package com.dss.tennis.tournament.tables.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class ContestDTO {

    private int id;
    private Integer winner;
    private ScoreDTO scoreDto;

    //get word is missing due to model mapper which is trying to access GetParticipant constructor
    public abstract Integer participantOneId();

    public abstract Integer participantTwoId();
}
