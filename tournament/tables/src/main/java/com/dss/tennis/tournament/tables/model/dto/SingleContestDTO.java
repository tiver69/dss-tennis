package com.dss.tennis.tournament.tables.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SingleContestDTO extends ContestDTO {

    private PlayerDTO playerOne;
    private PlayerDTO playerTwo;

    @Override
    public Integer participantOneId() {
        return playerOne.getId();
    }

    @Override
    public Integer participantTwoId() {
        return playerTwo.getId();
    }
}
