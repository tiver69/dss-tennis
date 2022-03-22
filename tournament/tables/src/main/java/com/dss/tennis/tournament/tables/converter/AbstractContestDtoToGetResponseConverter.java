package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetParticipant;
import com.dss.tennis.tournament.tables.model.response.v1.GetScore;
import com.dss.tennis.tournament.tables.model.response.v1.GetScore.GetSetScoreAttributes;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject;

import java.util.Map;

import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.SET_SCORE;

public abstract class AbstractContestDtoToGetResponseConverter<T extends ContestDTO, E extends GetParticipant> {

    public void convertSuper(T sourceContest, GetContest<E> destinationContest) {
        if (sourceContest.getScoreDto() == null) return;
        GetScore getScore = new GetScore();

        Map<SetType, SetScoreDTO> sets = sourceContest.getScoreDto().getSets();
        sets.keySet().forEach(key -> populateDestinationScore(sets.get(key), key, getScore));
        destinationContest.setScore(getScore);
    }

    private void populateDestinationScore(SetScoreDTO setScoreDto, SetType setType, GetScore destinationScore) {
        switch (setType) {
            case SET_ONE:
                destinationScore.setSetOne(mapSetScore(setScoreDto));
                break;
            case SET_TWO:
                destinationScore.setSetTwo(mapSetScore(setScoreDto));
                break;
            case SET_THREE:
                destinationScore.setSetThree(mapSetScore(setScoreDto));
                break;
            case TIE_BREAK:
                destinationScore.setTieBreak(mapSetScore(setScoreDto));
                break;
        }
    }

    private ResourceObject<GetSetScoreAttributes> mapSetScore(SetScoreDTO setSourceScore) {
        ResourceObject<GetSetScoreAttributes> resultScore = new ResourceObject<>();
        resultScore.setId(setSourceScore.getId());
        resultScore.setType(SET_SCORE.value);
        GetSetScoreAttributes attributes = new GetSetScoreAttributes(setSourceScore
                .getParticipantOneScore(), setSourceScore.getParticipantTwoScore());
        resultScore.setAttributes(attributes);

        return resultScore;
    }
}
