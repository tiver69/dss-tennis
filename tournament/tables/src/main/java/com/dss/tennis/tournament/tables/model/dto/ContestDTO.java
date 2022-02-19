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
    private Byte setOnePlayerTwo;
    private Byte setOnePlayerOne;
    private Byte setTwoPlayerOne;
    private Byte setTwoPlayerTwo;
    private Byte setThreePlayerOne;
    private Byte setThreePlayerTwo;
    private Byte tieBreakPlayerOne;
    private Byte tieBreakPlayerTwo;
}
