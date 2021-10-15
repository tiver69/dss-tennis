package com.dss.tennis.tournament.tables.model.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContestDTO {

    private int id;
    private PlayerDTO playerOne;
    private PlayerDTO playerTwo;

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
