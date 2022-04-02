package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SetScoreRepository extends CrudRepository<SetScore, Integer> {

    List<SetScore> findByContestId(Integer scoreId);

    @Modifying(clearAutomatically = true)
    @Query("update SetScore ss set ss.participantOne = ?1, ss.participantTwo = ?2 where ss.id = ?3")
    void updateSetScoreById(Byte participantOne, Byte participantTwo, Integer setScoreId);

    @Modifying(clearAutomatically = true)
    @Query("delete from SetScore ss where ss.contest.id = ?1")
    void removeByContestId(Integer contestId);

    @Modifying(clearAutomatically = true)
    @Query("delete from SetScore ss where ss.id = ?1")
    void removeById(Integer setScoreId);
}
