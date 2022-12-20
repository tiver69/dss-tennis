package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import static com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.QueryMethod.*;
import static com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.ResultType.PAGE;
import static com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.ResultType.SINGLE_RECORD;

public interface TournamentRepository extends CrudRepository<Tournament, Integer> {

    @RepositoryLogRecord(method = GET, resultType = SINGLE_RECORD)
    @Query("select t.id from Tournament t where t.name = ?1")
    Integer getTournamentIdByName(String name);

    @RepositoryLogRecord(method = GET, resultType = PAGE)
    Page<Tournament> findAll(Pageable pageable);

    @Override
    @RepositoryLogRecord(method = SAVE)
    <S extends Tournament> S save(S tournament);

    @Override
    @RepositoryLogRecord(method = GET, resultType = SINGLE_RECORD)
    Optional<Tournament> findById(Integer id);

    @Override
    @RepositoryLogRecord(method = DELETE)
    void deleteById(Integer integer);
}
