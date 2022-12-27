package ua.com.dss.tennis.tournament.api.model.db.v2;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.com.dss.tennis.tournament.api.model.db.v1.Team;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "contest_id")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DoubleContest extends Contest {

    private Team teamOne;
    private Team teamTwo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_one_id", nullable = false)
    public Team getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(Team teamOne) {
        this.teamOne = teamOne;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_two_id", nullable = false)
    public Team getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(Team teamTwo) {
        this.teamTwo = teamTwo;
    }

    @Override
    public String toString() {
        return "DoubleContest{" +
                super.toString() +
                ", teamOne=" + teamOne +
                ", teamTwo=" + teamTwo +
                '}';
    }
}
