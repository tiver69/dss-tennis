package ua.com.dss.tennis.tournament.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord;
import ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord.QueryMethod;
import ua.com.dss.tennis.tournament.api.model.db.v1.Player;

import java.util.List;
import java.util.Optional;

import static ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord.QueryMethod.GET;
import static ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord.QueryMethod.SAVE;
import static ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord.ResultType.*;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

    @RepositoryLogRecord(method = GET, resultType = SINGLE_RECORD)
    Optional<Player> findByFirstNameAndLastName(String firstName, String lastName);

    @RepositoryLogRecord(method = QueryMethod.IS_QUERY)
    @Query("SELECT COUNT(p.id) > 0 FROM Player p " +
            "RIGHT JOIN SingleContest sc ON (sc.playerOneId=p.id OR sc.playerTwoId=p.id) " +
            "WHERE sc.tournamentId=:tournamentId AND p.id=:playerId")
    boolean isPlayerEnrolledToTournament(@Param("playerId") Integer playerId,
                                         @Param("tournamentId") Integer tournamentId);

    @RepositoryLogRecord(method = GET, resultType = MULTIPLE_RECORDS)
    @Query("SELECT DISTINCT p.id FROM Player p " +
            "RIGHT JOIN SingleContest sc ON (sc.playerOneId=p.id OR sc.playerTwoId=p.id) " +
            "WHERE sc.tournamentId=:tournamentId")
    List<Integer> findPlayerIdsBySingleTournamentId(@Param("tournamentId") Integer singleTournamentId);

    @RepositoryLogRecord(method = GET, resultType = MULTIPLE_RECORDS)
    @Query("SELECT DISTINCT p.id FROM Player p " +
            "RIGHT JOIN Team team ON (team.playerOneId=p.id OR team.playerTwoId=p.id) " +
            "RIGHT JOIN DoubleContest dc ON (dc.teamOne=team OR dc.teamTwo=team) " +
            "WHERE dc.tournamentId=:tournamentId")
    List<Integer> findPlayerIdsByDoubleTournamentId(@Param("tournamentId") Integer doubleTournamentId);

    @RepositoryLogRecord(method = GET, resultType = MULTIPLE_RECORDS)
    @Query("SELECT DISTINCT p FROM Player p " +
            "RIGHT JOIN SingleContest sc ON (sc.playerOneId=p.id OR sc.playerTwoId=p.id) " +
            "WHERE sc.tournamentId=:tournamentId")
    List<Player> findPlayersBySingleTournamentId(@Param("tournamentId") Integer singleTournamentId);

    @RepositoryLogRecord(method = GET, resultType = MULTIPLE_RECORDS)
    @Query("SELECT DISTINCT p FROM Player p " +
            "RIGHT JOIN Team team ON (team.playerOneId=p.id OR team.playerTwoId=p.id) " +
            "RIGHT JOIN DoubleContest dc ON (dc.teamOne=team OR dc.teamTwo=team) " +
            "WHERE dc.tournamentId=:tournamentId")
    List<Player> findPlayersByDoubleTournamentId(@Param("tournamentId") Integer doubleTournamentId);

    @Override
    @RepositoryLogRecord(method = GET, resultType = SINGLE_RECORD)
    Optional<Player> findById(Integer id);

    @Override
    @RepositoryLogRecord(method = SAVE)
    <S extends Player> S save(S player);

    @RepositoryLogRecord(method = GET, resultType = PAGE)
    Page<Player> findAll(Pageable pageable);

    @Override
    @RepositoryLogRecord(method = QueryMethod.IS_QUERY)
    boolean existsById(Integer id);
}
