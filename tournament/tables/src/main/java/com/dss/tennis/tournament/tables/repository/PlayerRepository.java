package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.db.v1.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

    Optional<Player> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT DISTINCT p FROM Player p " +
            "RIGHT JOIN SingleContest sc ON (sc.playerOneId=p.id OR sc.playerTwoId=p.id) " +
            "RIGHT JOIN Tournament t ON t=sc.tournament " +
            "WHERE t.id=:tournamentId")
    List<Player> findPlayersBySingleTournamentId(@Param("tournamentId") Integer tournamentId);

    @Query("SELECT DISTINCT p FROM Player p " +
            "RIGHT JOIN Team team ON (team.playerOneId=p.id OR team.playerTwoId=p.id) " +
            "RIGHT JOIN DoubleContest dc ON (dc.teamOne=team OR dc.teamTwo=team) " +
            "RIGHT JOIN Tournament t ON t=dc.tournament " +
            "WHERE t.id=:tournamentId")
    List<Player> findPlayersByDoubleTournamentId(@Param("tournamentId") Integer tournamentId);



}
