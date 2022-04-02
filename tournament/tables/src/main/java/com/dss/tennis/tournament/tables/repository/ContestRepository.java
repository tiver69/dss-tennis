package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ContestRepository extends CrudRepository<Contest, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("update Contest c set c.winner = ?1 where c.id = ?2")
    void updateWinnerIdByContestId(Integer winnerId, Integer contestId);

    @Modifying(clearAutomatically = true)
    @Query("update Contest c set c.winner = ?1, c.techDefeat = ?2 where c.id = ?3")
    void updateTechDefeatByContestId(Integer winnerId, boolean isTechDefeat, Integer contestId);

    @Query("select count(c) > 0 from Contest c where c.tournamentId = ?1")
    boolean isTournamentHasContests(Integer tournamentId);

    @Query("SELECT sc FROM SingleContest sc WHERE (sc.playerOneId = ?1 OR sc.playerTwoId = ?1) AND sc.tournamentId = " +
            "?2")
    List<Contest> findByPlayerIdAndSingleTournamentId(Integer playerId, Integer tournamentId);

    @Query("SELECT dc FROM DoubleContest dc WHERE (dc.teamOne.id = ?1 OR dc.teamTwo.id = ?1) AND dc.tournamentId = ?2")
    List<Contest> findByTeamIdAndDoubleTournamentId(Integer teamId, Integer tournamentId);

    Optional<Contest> findByIdAndTournamentId(Integer id, Integer tournamentId);

    List<Contest> findByTournamentId(Integer TournamentId);
}
