package com.dss.tennis.tournament.tables.model.db.v2;

import com.dss.tennis.tournament.tables.model.db.v1.Score;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Contest {
    private int id;
    private byte round;
    private Score score;
    private Tournament tournament;
    private Byte winner;
    private Date date;

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
    @Column(name = "round", nullable = false)
    public byte getRound() {
        return round;
    }

    public void setRound(byte round) {
        this.round = round;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "score_id", nullable = false)
    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    @Basic
    @Column(name = "winner")
    public Byte getWinner() {
        return winner;
    }

    public void setWinner(Byte winner) {
        this.winner = winner;
    }

    @Basic
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contest contest = (Contest) o;

        if (id != contest.id) return false;
        if (round != contest.round) return false;
        if (winner != null ? !winner.equals(contest.winner) : contest.winner != null) return false;
        return date != null ? date.equals(contest.date) : contest.date == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) round;
        result = 31 * result + (winner != null ? winner.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
