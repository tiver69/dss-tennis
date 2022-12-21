package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord;
import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.QueryMethod;
import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.ResultType;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import static com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.QueryMethod.*;
import static com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.ResultType.SINGLE_RECORD;

public interface ContestRepository extends CrudRepository<Contest, Integer> {

    @RepositoryLogRecord(method = QueryMethod.UPDATE)
    @Modifying(clearAutomatically = true)
    @Query("update Contest c set c.winnerId = ?1 where c.id = ?2")
    int updateWinnerIdByContestId(Integer winnerId, Integer contestId);

    @RepositoryLogRecord(method = QueryMethod.IS_QUERY)
    @Query("select count(c) > 0 from Contest c where c.tournamentId = ?1")
    boolean isTournamentHasContests(Integer tournamentId);

    @RepositoryLogRecord(method = QueryMethod.GET, resultType = ResultType.MULTIPLE_RECORDS)
    @Query("SELECT sc FROM SingleContest sc WHERE (sc.playerOneId = ?1 OR sc.playerTwoId = ?1) AND sc.tournamentId = " +
            "?2")
    List<Contest> findByPlayerIdAndSingleTournamentId(Integer playerId, Integer tournamentId);

    @RepositoryLogRecord(method = QueryMethod.GET, resultType = ResultType.MULTIPLE_RECORDS)
    @Query("SELECT dc FROM DoubleContest dc WHERE (dc.teamOne.id = ?1 OR dc.teamTwo.id = ?1) AND dc.tournamentId = ?2")
    List<Contest> findByTeamIdAndDoubleTournamentId(Integer teamId, Integer tournamentId);

    @RepositoryLogRecord(method = QueryMethod.IS_QUERY)
    @Query("SELECT CASE WHEN c.participantOneScore.setOne = 0 AND c.participantOneScore.setTwo = 0 AND c.participantOneScore.setThree = 0 AND c.participantOneScore.tieBreak = 0 " +
            "AND c.participantTwoScore.setOne = 0 AND c.participantTwoScore.setTwo = 0 AND c.participantTwoScore.setThree = 0 AND c.participantTwoScore.tieBreak = 0 " +
            "THEN false ELSE true END " +
            "FROM Contest c WHERE c.id = (SELECT ec.id FROM EliminationContest ec WHERE ec.firstParentContestId = ?1 OR ec.secondParentContestId = ?1)")
    boolean isEliminationContestChildScoreDefined(Integer contestId);

    @RepositoryLogRecord(method = QueryMethod.IS_QUERY)
//    SELECT * From set_score where contest_id in (SELECT contest_id from elimination_contest ec where ec.first_parent_contest_id = 285 OR ec.second_parent_contest_id = 285)
    @Query("SELECT CASE WHEN c.participantOneScore.techDefeat = 1 OR c.participantTwoScore.techDefeat = 1 THEN true ELSE false END " +
            "FROM Contest c WHERE c.id = (SELECT ec.id FROM EliminationContest ec WHERE ec.firstParentContestId = ?1 OR ec" +
            ".secondParentContestId = ?1)")
    boolean isEliminationContestChildTechDefeat(Integer contestId);

    @RepositoryLogRecord(method = QueryMethod.IS_QUERY)
    @Query("SELECT CASE WHEN count(c) = 1 THEN false ELSE true END FROM Contest c " +
            "WHERE c.id = (SELECT ec.id FROM EliminationContest ec WHERE ec.firstParentContestId = ?1 OR ec" +
            ".secondParentContestId = ?1)")
    boolean isEliminationContestFinal(Integer contestId);

    @RepositoryLogRecord(method = QueryMethod.GET, resultType = SINGLE_RECORD)
    Optional<Contest> findByIdAndTournamentId(Integer id, Integer tournamentId);

    @RepositoryLogRecord(method = QueryMethod.GET, resultType = ResultType.MULTIPLE_RECORDS)
    List<Contest> findByTournamentId(Integer tournamentId);

    @Override
    @RepositoryLogRecord(method = GET, resultType = SINGLE_RECORD)
    Optional<Contest> findById(Integer id);

    @Override
    @RepositoryLogRecord(method = SAVE)
    <S extends Contest> S save(S contest);

    @Override
    @RepositoryLogRecord(method = DELETE)
    void delete(Contest contest);

    @Override
    @RepositoryLogRecord(method = DELETE)
    void deleteById(Integer integer);
}
