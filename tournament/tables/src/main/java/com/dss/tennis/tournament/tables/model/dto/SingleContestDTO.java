package com.dss.tennis.tournament.tables.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SingleContestDTO extends ContestDTO {

    private PlayerDTO playerOne;
    private PlayerDTO playerTwo;
}
