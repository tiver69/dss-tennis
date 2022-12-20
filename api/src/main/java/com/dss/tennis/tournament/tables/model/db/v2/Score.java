package com.dss.tennis.tournament.tables.model.db.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Score {

    private int id;
    private Byte setOne;
    private Byte setTwo;
    private Byte setThree;
    private Byte tieBreak;
    private Boolean techDefeat;

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
    @Column(name = "set_one")
    public Byte getSetOne() {
        return setOne;
    }

    public void setSetOne(Byte setOne) {
        this.setOne = setOne;
    }

    @Basic
    @Column(name = "set_two")
    public Byte getSetTwo() {
        return setTwo;
    }

    public void setSetTwo(Byte setTwo) {
        this.setTwo = setTwo;
    }


    @Basic
    @Column(name = "set_three")
    public Byte getSetThree() {
        return setThree;
    }

    public void setSetThree(Byte setThree) {
        this.setThree = setThree;
    }

    @Basic
    @Column(name = "tie_break")
    public Byte getTieBreak() {
        return tieBreak;
    }

    public void setTieBreak(Byte tieBreak) {
        this.tieBreak = tieBreak;
    }

    @Basic
    @Column(name = "tech_defeat")
    public Boolean getTechDefeat() {
        return techDefeat;
    }

    public void setTechDefeat(Boolean techDefeat) {
        this.techDefeat = techDefeat;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", setOne=" + setOne +
                ", setTwo=" + setTwo +
                ", setThree=" + setThree +
                ", tieBreak=" + tieBreak +
                ", techDefeat=" + techDefeat +
                '}';
    }
}
