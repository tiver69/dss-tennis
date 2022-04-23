package com.dss.tennis.tournament.tables.model.db.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tournament {
    private int id;
    private String name;
    private TournamentType tournamentType;
    private ParticipantType participantType;
    private StatusType status;
    private LocalDate beginningDate;

    public Tournament(int id) {
        this.id = id;
    }

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
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "tournament_type", nullable = false)
    public TournamentType getTournamentType() {
        return tournamentType;
    }

    public void setTournamentType(TournamentType tournamentType) {
        this.tournamentType = tournamentType;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "participant_type", nullable = false)
    public ParticipantType getParticipantType() {
        return participantType;
    }

    public void setParticipantType(ParticipantType participantType) {
        this.participantType = participantType;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    @Basic
    @Column(name = "beginning_date", nullable = false)
    public LocalDate getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(LocalDate beginningDate) {
        this.beginningDate = beginningDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return id == that.id && Objects
                .equals(name, that.name) && tournamentType == that.tournamentType && participantType == that.participantType && status == that.status && Objects
                .equals(beginningDate, that.beginningDate);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tournamentType != null ? tournamentType.hashCode() : 0);
        result = 31 * result + (participantType != null ? participantType.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return 31 * result + (beginningDate != null ? beginningDate.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tournamentType=" + tournamentType +
                ", participantType=" + participantType +
                ", status=" + status +
                ", beginningDate=" + beginningDate +
                '}';
    }
}
