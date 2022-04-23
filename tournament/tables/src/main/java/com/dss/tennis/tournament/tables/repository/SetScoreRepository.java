package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord;
import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.QueryMethod;
import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.ResultType;
import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import static com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.QueryMethod.SAVE;

public interface SetScoreRepository extends CrudRepository<SetScore, Integer> {

    @Override
    @RepositoryLogRecord(method = SAVE)
    <S extends SetScore> S save(S setScore);

    @RepositoryLogRecord(method = QueryMethod.GET, resultType = ResultType.MULTIPLE_RECORDS)
    List<SetScore> findByContestId(Integer contestId);

    @RepositoryLogRecord(method = QueryMethod.UPDATE)
    @Modifying(clearAutomatically = true)
    @Query("update SetScore ss set ss.participantOne = ?1, ss.participantTwo = ?2 where ss.id = ?3")
    int updateSetScoreById(Byte participantOne, Byte participantTwo, Integer setScoreId);

    @RepositoryLogRecord(method = QueryMethod.DELETE)
    @Modifying(clearAutomatically = true)
    @Query("delete from SetScore ss where ss.contest.id = ?1")
    void removeByContestId(Integer contestId);

    @RepositoryLogRecord(method = QueryMethod.DELETE)
    @Modifying(clearAutomatically = true)
    @Query("delete from SetScore ss where ss.id = ?1")
    void removeById(Integer setScoreId);
}
