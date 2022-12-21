package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord;
import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.QueryMethod;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.QueryMethod.GET;
import static com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.QueryMethod.SAVE;
import static com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.ResultType.*;

public interface TeamRepository extends CrudRepository<Team, Integer> {

    @RepositoryLogRecord(method = QueryMethod.IS_QUERY)
    @Query("SELECT COUNT(team.id) > 0 FROM Team team " +
            "RIGHT JOIN DoubleContest dc ON (dc.teamOne=team OR dc.teamTwo=team) " +
            "WHERE dc.tournamentId=:tournamentId AND team.id=:teamId")
    boolean isTeamEnrolledToTournament(@Param("teamId") Integer teamId,
                                       @Param("tournamentId") Integer tournamentId);

    @RepositoryLogRecord(method = QueryMethod.GET, resultType = MULTIPLE_RECORDS)
    @Query("SELECT DISTINCT team FROM Team team " +
            "RIGHT JOIN DoubleContest dc ON (dc.teamOne.id=team.id OR dc.teamTwo.id=team.id) " +
            "WHERE dc.tournamentId=:tournamentId")
    List<Team> findTeamsByDoubleTournamentId(@Param("tournamentId") Integer tournamentId);

    @RepositoryLogRecord(method = QueryMethod.GET, resultType = MULTIPLE_RECORDS)
    @Query("SELECT DISTINCT team.id FROM Team team " +
            "RIGHT JOIN DoubleContest dc ON (dc.teamOne.id=team.id OR dc.teamTwo.id=team.id) " +
            "WHERE dc.tournamentId=:tournamentId")
    List<Integer> findTeamIdsByDoubleTournamentId(@Param("tournamentId") Integer tournamentId);

    @Query("SELECT COUNT(DISTINCT team) FROM Team team " +
            "RIGHT JOIN DoubleContest dc ON (dc.teamOne.id=team.id OR dc.teamTwo.id=team.id) " +
            "WHERE dc.tournamentId=:tournamentId")
    int countTeamsByDoubleTournamentId(@Param("tournamentId") Integer tournamentId);

    @RepositoryLogRecord(method = QueryMethod.GET, resultType = SINGLE_RECORD)
    @Query("SELECT team FROM Team team WHERE (team.playerOneId = ?1 AND team.playerTwoId = ?2) OR (team.playerOneId =" +
            " ?2 AND team.playerTwoId = ?1)")
    Optional<Team> getTeamByPlayerIds(Integer playerOneId, Integer playerTwoId);

    @Override
    @RepositoryLogRecord(method = SAVE)
    <S extends Team> S save(S team);

    @RepositoryLogRecord(method = QueryMethod.GET, resultType = PAGE)
    Page<Team> findAll(Pageable pageable);

    @Override
    @RepositoryLogRecord(method = GET, resultType = SINGLE_RECORD)
    Optional<Team> findById(Integer id);

    @Override
    @RepositoryLogRecord(method = QueryMethod.IS_QUERY)
    boolean existsById(Integer id);
}
