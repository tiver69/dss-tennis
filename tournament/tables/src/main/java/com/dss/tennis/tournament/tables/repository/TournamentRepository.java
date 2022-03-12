package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TournamentRepository extends CrudRepository<Tournament, Integer> {

    Optional<Tournament> findByName(String name);

    Page<Tournament> findAll(Pageable pageable);
}
