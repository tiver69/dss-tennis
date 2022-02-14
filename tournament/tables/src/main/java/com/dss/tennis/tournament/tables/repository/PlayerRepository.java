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
            "RIGHT JOIN Tournament t ON t.id=sc.tournament.id " +
            "WHERE t.id=:tournamentId")
    List<Player> findPlayersByTournamentId(@Param("tournamentId") Integer tournamentId);

}
