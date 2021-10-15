package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.db.v1.Contest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContestRepository extends CrudRepository<Contest, Integer> {

    List<Contest> findByTournamentId(Integer TournamentId);
}
