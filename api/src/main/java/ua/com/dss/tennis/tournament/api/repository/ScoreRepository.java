package ua.com.dss.tennis.tournament.api.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord;
import ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord.QueryMethod;
import ua.com.dss.tennis.tournament.api.model.db.v2.Score;

import static ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord.QueryMethod.SAVE;

public interface ScoreRepository extends CrudRepository<Score, Integer> {

    @Override
    @RepositoryLogRecord(method = SAVE)
    Score save(Score score);

    @RepositoryLogRecord(method = QueryMethod.UPDATE)
    @Modifying(clearAutomatically = true)
    @Query("update Score s set s.setOne = ?1, s.setTwo = ?2, s.setThree = ?3, s.tieBreak = ?4, s.techDefeat = ?5 " +
            "where s.id = ?6")
    int updateSetScoreById(Byte setOneScore, Byte setTwoScore, Byte setThreeScore, Byte tieBreakScore,
                           Boolean techDefeat, Integer scoreId);

    @RepositoryLogRecord(method = QueryMethod.UPDATE)
    @Modifying(clearAutomatically = true)
    @Query("update Score s set s.techDefeat = 1 where s.id = ?1")
    int setTechDefeatByScoreById(Integer scoreId);
}
