package com.dss.tennis.tournament.tables.model.db.v2;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "contest_id")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SingleContest extends Contest {

    private Integer playerOneId;
    private Integer playerTwoId;

    @Basic
    @Column(name = "player_one_id", nullable = false)
    public Integer getPlayerOneId() {
        return playerOneId;
    }

    public void setPlayerOneId(Integer playerOneId) {
        this.playerOneId = playerOneId;
    }

    @Basic
    @Column(name = "player_two_id", nullable = false)
    public Integer getPlayerTwoId() {
        return playerTwoId;
    }

    public void setPlayerTwoId(Integer playerTwoId) {
        this.playerTwoId = playerTwoId;
    }
}
