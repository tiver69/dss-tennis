package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.db.v1.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Integer> {

    @Query("SELECT DISTINCT team FROM Team team " +
            "RIGHT JOIN DoubleContest dc ON (dc.teamOne.id=team.id OR dc.teamTwo.id=team.id) " +
            "WHERE dc.tournamentId=:tournamentId")
    List<Team> findTeamsByDoubleTournamentId(@Param("tournamentId") Integer tournamentId);
}
