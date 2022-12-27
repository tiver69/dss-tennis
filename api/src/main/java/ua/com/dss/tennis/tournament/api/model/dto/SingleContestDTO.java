package ua.com.dss.tennis.tournament.api.model.dto;

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
    public Integer getParticipantOneId() {
        return playerOne == null ? null : playerOne.getId();
    }

    @Override
    public Integer getParticipantTwoId() {
        return playerTwo == null ? null : playerTwo.getId();
    }

    @Override
    public Object getParticipantOne() {
        return this.getPlayerOne();
    }

    @Override
    public Object getParticipantTwo() {
        return this.getPlayerTwo();
    }
}
