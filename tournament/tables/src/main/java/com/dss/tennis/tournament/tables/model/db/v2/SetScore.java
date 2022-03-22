package com.dss.tennis.tournament.tables.model.db.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetScore {

    private int id;
    private SetType setType;
    private Byte participantOne;
    private Byte participantTwo;
    private TieBreak tieBreak;
    private Contest contest;

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
    @Enumerated(EnumType.STRING)
    @Column(name = "set_type", nullable = false)
    public SetType getSetType() {
        return setType;
    }

    public void setSetType(SetType setType) {
        this.setType = setType;
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
    @Column(name = "participant_Two")
    public Byte getParticipantTwo() {
        return participantTwo;
    }

    public void setParticipantTwo(Byte participantTwo) {
        this.participantTwo = participantTwo;
    }

    @OneToOne(mappedBy = "setScore", fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public TieBreak getTieBreak() {
        return tieBreak;
    }

    public void setTieBreak(TieBreak tieBreak) {
        this.tieBreak = tieBreak;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }
}
