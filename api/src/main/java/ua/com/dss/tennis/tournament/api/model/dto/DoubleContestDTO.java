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
public class DoubleContestDTO extends ContestDTO {

    private TeamDTO teamOne;
    private TeamDTO teamTwo;

    @Override
    public Integer getParticipantOneId() {
        return teamOne.getId();
    }

    @Override
    public Integer getParticipantTwoId() {
        return teamTwo.getId();
    }

    @Override
    public Object getParticipantOne() {
        return this.getTeamOne();
    }

    @Override
    public Object getParticipantTwo() {
        return this.getTeamTwo();
    }
}
