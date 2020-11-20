package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.v1.Score;
import org.springframework.data.repository.CrudRepository;

public interface ScoreRepository extends CrudRepository<Score, Integer> {
}
