package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<Tournament, Integer> {

    @Query("select t.id from Tournament t where t.name = ?1")
    Integer getTournamentIdByName(String name);

    Page<Tournament> findAll(Pageable pageable);
}
