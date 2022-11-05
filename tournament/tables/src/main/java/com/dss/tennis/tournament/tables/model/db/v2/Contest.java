package com.dss.tennis.tournament.tables.model.db.v2;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Contest {
    private int id;
    private Integer tournamentId;
    private Integer winnerId;
    private boolean techDefeat;
    private Date date;
    private Set<SetScore> sets;

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
    @Column(name = "tech_defeat")
    public boolean isTechDefeat() {
        return techDefeat;
    }

    public void setTechDefeat(boolean techDefeat) {
        this.techDefeat = techDefeat;
    }

    @Basic
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "contest_id")
    public Set<SetScore> getSets() {
        return sets;
    }

    public void setSets(Set<SetScore> sets) {
        this.sets = sets;
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
        return "id=" + id +
                ", tournamentId=" + tournamentId +
                ", winner=" + winnerId +
                ", techDefeat=" + techDefeat +
                ", date=" + date;
    }
}
