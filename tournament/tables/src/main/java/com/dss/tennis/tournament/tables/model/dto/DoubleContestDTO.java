package com.dss.tennis.tournament.tables.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DoubleContestDTO extends ContestDTO {

    private TeamDTO teamOne;
    private TeamDTO teamTwo;
}
