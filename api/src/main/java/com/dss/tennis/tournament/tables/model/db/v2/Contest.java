package com.dss.tennis.tournament.tables.model.db.v2;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Contest {
    private int id;
    private Integer tournamentId;
    private Integer winnerId;
    private Date date;
    private Score participantOneScore;
    private Score participantTwoScore;

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
    @Column(name = "tournament_id", nullable = false)
    public Integer getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    @Basic
    @Column(name = "winner_id")
    public Integer getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }

    @Basic
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "participant_one_score_id")
    public Score getParticipantOneScore() {
        return participantOneScore;
    }

    public void setParticipantOneScore(Score participantOneScore) {
        this.participantOneScore = participantOneScore;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "participant_two_score_id")
    public Score getParticipantTwoScore() {
        return participantTwoScore;
    }

    public void setParticipantTwoScore(Score participantTwoScore) {
        this.participantTwoScore = participantTwoScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contest contest = (Contest) o;

        if (id != contest.id) return false;
        if (winnerId != null ? !winnerId.equals(contest.winnerId) : contest.winnerId != null) return false;
        if (tournamentId != null ? !tournamentId.equals(contest.tournamentId) : contest.tournamentId != null)
            return false;
        return date != null ? date.equals(contest.date) : contest.date == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (winnerId != null ? winnerId.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (tournamentId != null ? tournamentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "id=" + id +
                ", tournamentId=" + tournamentId +
                ", winnerId=" + winnerId +
                ", date=" + date +
                ", participantOneScore=" + participantOneScore +
                ", participantTwoScore=" + participantTwoScore +
                '}';
    }
}
