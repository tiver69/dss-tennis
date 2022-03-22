package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ContestRepository extends CrudRepository<Contest, Integer> {

    @Modifying
    @Query("update Contest c set c.winner = ?1 where c.id = ?2")
    void updateWinnerIdByContestId(Integer winnerId, Integer contestId);

    Optional<Contest> findByIdAndTournamentId(Integer id, Integer tournamentId);

    List<Contest> findByTournamentId(Integer TournamentId);
}
