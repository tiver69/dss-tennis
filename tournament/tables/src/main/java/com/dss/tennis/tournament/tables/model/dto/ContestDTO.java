package com.dss.tennis.tournament.tables.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class ContestDTO {

    private int id;
    private int scoreId;
    private Byte setOneParticipantOne;
    private Byte setOneParticipantTwo;
    private Byte setTwoParticipantOne;
    private Byte setTwoParticipantTwo;
    private Byte setThreeParticipantOne;
    private Byte setThreeParticipantTwo;
    private Byte tieBreakParticipantOne;
    private Byte tieBreakParticipantTwo;
}
