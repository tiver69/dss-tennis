package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SetScoreRepository extends CrudRepository<SetScore, Integer> {

    List<SetScore> findByContestId(Integer scoreId);
}
