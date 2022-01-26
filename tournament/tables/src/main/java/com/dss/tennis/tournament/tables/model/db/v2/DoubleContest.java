package com.dss.tennis.tournament.tables.model.db.v2;

import com.dss.tennis.tournament.tables.model.db.v1.Team;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "contest_id")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DoubleContest extends Contest {

    private Team teamOne;
    private Team teamTwo;

    @ManyToOne
    @JoinColumn(name = "team_one_id", nullable = false)
    public Team getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(Team teamOne) {
        this.teamOne = teamOne;
    }

    @ManyToOne
    @JoinColumn(name = "team_two_id", nullable = false)
    public Team getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(Team teamTwo) {
        this.teamTwo = teamTwo;
    }
}
