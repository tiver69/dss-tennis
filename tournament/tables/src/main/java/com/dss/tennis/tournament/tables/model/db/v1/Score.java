package com.dss.tennis.tournament.tables.model.db.v1;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Score {
    private int id;
    private Byte setOneParticipantOne;
    private Byte setOneParticipantTwo;
    private Byte setTwoParticipantOne;
    private Byte setTwoParticipantTwo;
    private Byte setThreeParticipantOne;
    private Byte setThreeParticipantTwo;
    private Byte tieBreakParticipantOne;
    private Byte tieBreakParticipantTwo;

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
    @Column(name = "set_one_participant_one")
    public Byte getSetOneParticipantOne() {
        return setOneParticipantOne;
    }

    public void setSetOneParticipantOne(Byte setOneParticipantOne) {
        this.setOneParticipantOne = setOneParticipantOne;
    }

    @Basic
    @Column(name = "set_one_participant_two")
    public Byte getSetOneParticipantTwo() {
        return setOneParticipantTwo;
    }

    public void setSetOneParticipantTwo(Byte setOneParticipantTwo) {
        this.setOneParticipantTwo = setOneParticipantTwo;
    }

    @Basic
    @Column(name = "set_two_participant_one")
    public Byte getSetTwoParticipantOne() {
        return setTwoParticipantOne;
    }

    public void setSetTwoParticipantOne(Byte setTwoParticipantOne) {
        this.setTwoParticipantOne = setTwoParticipantOne;
    }

    @Basic
    @Column(name = "set_two_participant_two")
    public Byte getSetTwoParticipantTwo() {
        return setTwoParticipantTwo;
    }

    public void setSetTwoParticipantTwo(Byte setTwoParticipantTwo) {
        this.setTwoParticipantTwo = setTwoParticipantTwo;
    }

    @Basic
    @Column(name = "set_three_participant_one")
    public Byte getSetThreeParticipantOne() {
        return setThreeParticipantOne;
    }

    public void setSetThreeParticipantOne(Byte setThreeParticipantOne) {
        this.setThreeParticipantOne = setThreeParticipantOne;
    }

    @Basic
    @Column(name = "set_three_participant_two")
    public Byte getSetThreeParticipantTwo() {
        return setThreeParticipantTwo;
    }

    public void setSetThreeParticipantTwo(Byte setThreeParticipantTwo) {
        this.setThreeParticipantTwo = setThreeParticipantTwo;
    }

    @Basic
    @Column(name = "tie_break_participant_one")
    public Byte getTieBreakParticipantOne() {
        return tieBreakParticipantOne;
    }

    public void setTieBreakParticipantOne(Byte tieBreakParticipantOne) {
        this.tieBreakParticipantOne = tieBreakParticipantOne;
    }

    @Basic
    @Column(name = "tie_break_participant_two")
    public Byte getTieBreakParticipantTwo() {
        return tieBreakParticipantTwo;
    }

    public void setTieBreakParticipantTwo(Byte tieBreakParticipantTwo) {
        this.tieBreakParticipantTwo = tieBreakParticipantTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score = (Score) o;

        if (id != score.id) return false;
        if (!Objects.equals(setOneParticipantOne, score.setOneParticipantOne))
            return false;
        if (!Objects.equals(setOneParticipantTwo, score.setOneParticipantTwo))
            return false;
        if (!Objects.equals(setTwoParticipantOne, score.setTwoParticipantOne))
            return false;
        if (!Objects.equals(setTwoParticipantTwo, score.setTwoParticipantTwo))
            return false;
        if (!Objects.equals(setThreeParticipantOne, score.setThreeParticipantOne))
            return false;
        if (!Objects.equals(setThreeParticipantTwo, score.setThreeParticipantTwo))
            return false;
        if (!Objects.equals(tieBreakParticipantOne, score.tieBreakParticipantOne))
            return false;
        return Objects.equals(tieBreakParticipantTwo, score.tieBreakParticipantTwo);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (setOneParticipantOne != null ? setOneParticipantOne.hashCode() : 0);
        result = 31 * result + (setOneParticipantTwo != null ? setOneParticipantTwo.hashCode() : 0);
        result = 31 * result + (setTwoParticipantOne != null ? setTwoParticipantOne.hashCode() : 0);
        result = 31 * result + (setTwoParticipantTwo != null ? setTwoParticipantTwo.hashCode() : 0);
        result = 31 * result + (setThreeParticipantOne != null ? setThreeParticipantOne.hashCode() : 0);
        result = 31 * result + (setThreeParticipantTwo != null ? setThreeParticipantTwo.hashCode() : 0);
        result = 31 * result + (tieBreakParticipantOne != null ? tieBreakParticipantOne.hashCode() : 0);
        result = 31 * result + (tieBreakParticipantTwo != null ? tieBreakParticipantTwo.hashCode() : 0);
        return result;
    }
}
