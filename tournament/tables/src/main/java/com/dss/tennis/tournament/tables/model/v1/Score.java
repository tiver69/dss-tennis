package com.dss.tennis.tournament.tables.model.v1;

import javax.persistence.*;

@Entity
public class Score {
    private int id;
    private Byte setOnePlayerOne;
    private Byte setOnePlayerTwo;
    private Byte setTwoPlayerOne;
    private Byte setTwoPlayerTwo;
    private Byte setThreePlayerOne;
    private Byte setThreePlayerTwo;
    private Byte tieBreakPlayerOne;
    private Byte tieBreakPlayerTwo;

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
    @Column(name = "set_one_player_one", nullable = true)
    public Byte getSetOnePlayerOne() {
        return setOnePlayerOne;
    }

    public void setSetOnePlayerOne(Byte setOnePlayerOne) {
        this.setOnePlayerOne = setOnePlayerOne;
    }

    @Basic
    @Column(name = "set_one_player_two", nullable = true)
    public Byte getSetOnePlayerTwo() {
        return setOnePlayerTwo;
    }

    public void setSetOnePlayerTwo(Byte setOnePlayerTwo) {
        this.setOnePlayerTwo = setOnePlayerTwo;
    }

    @Basic
    @Column(name = "set_two_player_one", nullable = true)
    public Byte getSetTwoPlayerOne() {
        return setTwoPlayerOne;
    }

    public void setSetTwoPlayerOne(Byte setTwoPlayerOne) {
        this.setTwoPlayerOne = setTwoPlayerOne;
    }

    @Basic
    @Column(name = "set_two_player_two", nullable = true)
    public Byte getSetTwoPlayerTwo() {
        return setTwoPlayerTwo;
    }

    public void setSetTwoPlayerTwo(Byte setTwoPlayerTwo) {
        this.setTwoPlayerTwo = setTwoPlayerTwo;
    }

    @Basic
    @Column(name = "set_three_player_one", nullable = true)
    public Byte getSetThreePlayerOne() {
        return setThreePlayerOne;
    }

    public void setSetThreePlayerOne(Byte setThreePlayerOne) {
        this.setThreePlayerOne = setThreePlayerOne;
    }

    @Basic
    @Column(name = "set_three_player_two", nullable = true)
    public Byte getSetThreePlayerTwo() {
        return setThreePlayerTwo;
    }

    public void setSetThreePlayerTwo(Byte setThreePlayerTwo) {
        this.setThreePlayerTwo = setThreePlayerTwo;
    }

    @Basic
    @Column(name = "tie_break_player_one", nullable = true)
    public Byte getTieBreakPlayerOne() {
        return tieBreakPlayerOne;
    }

    public void setTieBreakPlayerOne(Byte tieBreakPlayerOne) {
        this.tieBreakPlayerOne = tieBreakPlayerOne;
    }

    @Basic
    @Column(name = "tie_break_player_two", nullable = true)
    public Byte getTieBreakPlayerTwo() {
        return tieBreakPlayerTwo;
    }

    public void setTieBreakPlayerTwo(Byte tieBreakPlayerTwo) {
        this.tieBreakPlayerTwo = tieBreakPlayerTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score = (Score) o;

        if (id != score.id) return false;
        if (setOnePlayerOne != null ? !setOnePlayerOne.equals(score.setOnePlayerOne) : score.setOnePlayerOne != null)
            return false;
        if (setOnePlayerTwo != null ? !setOnePlayerTwo.equals(score.setOnePlayerTwo) : score.setOnePlayerTwo != null)
            return false;
        if (setTwoPlayerOne != null ? !setTwoPlayerOne.equals(score.setTwoPlayerOne) : score.setTwoPlayerOne != null)
            return false;
        if (setTwoPlayerTwo != null ? !setTwoPlayerTwo.equals(score.setTwoPlayerTwo) : score.setTwoPlayerTwo != null)
            return false;
        if (setThreePlayerOne != null ? !setThreePlayerOne.equals(score.setThreePlayerOne) : score.setThreePlayerOne != null)
            return false;
        if (setThreePlayerTwo != null ? !setThreePlayerTwo.equals(score.setThreePlayerTwo) : score.setThreePlayerTwo != null)
            return false;
        if (tieBreakPlayerOne != null ? !tieBreakPlayerOne.equals(score.tieBreakPlayerOne) : score.tieBreakPlayerOne != null)
            return false;
        if (tieBreakPlayerTwo != null ? !tieBreakPlayerTwo.equals(score.tieBreakPlayerTwo) : score.tieBreakPlayerTwo != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (setOnePlayerOne != null ? setOnePlayerOne.hashCode() : 0);
        result = 31 * result + (setOnePlayerTwo != null ? setOnePlayerTwo.hashCode() : 0);
        result = 31 * result + (setTwoPlayerOne != null ? setTwoPlayerOne.hashCode() : 0);
        result = 31 * result + (setTwoPlayerTwo != null ? setTwoPlayerTwo.hashCode() : 0);
        result = 31 * result + (setThreePlayerOne != null ? setThreePlayerOne.hashCode() : 0);
        result = 31 * result + (setThreePlayerTwo != null ? setThreePlayerTwo.hashCode() : 0);
        result = 31 * result + (tieBreakPlayerOne != null ? tieBreakPlayerOne.hashCode() : 0);
        result = 31 * result + (tieBreakPlayerTwo != null ? tieBreakPlayerTwo.hashCode() : 0);
        return result;
    }
}
