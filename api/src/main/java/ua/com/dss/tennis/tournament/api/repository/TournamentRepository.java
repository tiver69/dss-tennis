package ua.com.dss.tennis.tournament.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord;
import ua.com.dss.tennis.tournament.api.model.db.v1.Tournament;

import java.util.Optional;

import static ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord.QueryMethod.*;
import static ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord.ResultType.PAGE;
import static ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord.ResultType.SINGLE_RECORD;

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
