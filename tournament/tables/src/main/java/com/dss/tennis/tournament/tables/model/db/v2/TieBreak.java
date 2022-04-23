package com.dss.tennis.tournament.tables.model.db.v2;

import javax.persistence.*;

@Entity
public class TieBreak {

    private int id;
    private Byte participantOne;
    private Byte participantTwo;
    private SetScore setScore;

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
    @Column(name = "participant_one")
    public Byte getParticipantOne() {
        return participantOne;
    }

    public void setParticipantOne(Byte participantOne) {
        this.participantOne = participantOne;
    }

    @Basic
    @Column(name = "participant_two")
    public Byte getParticipantTwo() {
        return participantTwo;
    }

    public void setParticipantTwo(Byte participantTwo) {
        this.participantTwo = participantTwo;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "set_score_id")
    public SetScore getSetScore() {
        return setScore;
    }

    public void setSetScore(SetScore setScore) {
        this.setScore = setScore;
    }

    @Override
    public String toString() {
        return "TieBreak{" +
                "id=" + id +
                ", participantOne=" + participantOne +
                ", participantTwo=" + participantTwo +
                '}';
    }
}
