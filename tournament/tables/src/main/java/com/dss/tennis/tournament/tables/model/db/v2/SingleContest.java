package com.dss.tennis.tournament.tables.model.db.v2;

import com.dss.tennis.tournament.tables.model.db.v1.Player;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

//@Entity
@PrimaryKeyJoinColumn(name="contest_id")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SingleContest extends Contest {

    private Player playerOne;
    private Player playerTwo;

    @ManyToOne
    @JoinColumn(name = "player_one_id", nullable = false)
    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    @ManyToOne
    @JoinColumn(name = "player_two_id", nullable = false)
    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }
}
