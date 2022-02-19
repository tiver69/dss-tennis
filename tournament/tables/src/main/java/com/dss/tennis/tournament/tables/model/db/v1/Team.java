package com.dss.tennis.tournament.tables.model.db.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    private int id;
    private Integer playerOneId;
    private Integer playerTwoId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id && playerOneId.equals(team.playerOneId) && playerTwoId.equals(team.playerTwoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playerOneId, playerTwoId);
    }
}
