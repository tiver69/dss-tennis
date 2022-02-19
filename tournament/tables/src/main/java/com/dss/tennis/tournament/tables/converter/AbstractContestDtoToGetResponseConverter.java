package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest.Set;
import com.dss.tennis.tournament.tables.model.response.v1.GetParticipant;
import org.apache.commons.lang3.ObjectUtils;

public abstract class AbstractContestDtoToGetResponseConverter<T extends ContestDTO, E extends GetParticipant> {

    public void convertSuper(T sourceContest, GetContest<E> destinationContest) {
        destinationContest
                .setSetOne(createSetScore(sourceContest.getSetOneParticipantOne(), sourceContest
                        .getSetOneParticipantTwo()));
        destinationContest
                .setSetTwo(createSetScore(sourceContest.getSetTwoParticipantOne(), sourceContest
                        .getSetTwoParticipantTwo()));
        destinationContest
                .setSetThree(createSetScore(sourceContest.getSetThreeParticipantOne(), sourceContest
                        .getSetThreeParticipantTwo()));
        destinationContest
                .setTieBreak(createSetScore(sourceContest.getTieBreakParticipantOne(), sourceContest
                        .getTieBreakParticipantTwo()));
    }

    private Set createSetScore(Byte participantOneScore, Byte participantTwoScore) {
        if (participantOneScore != null || participantTwoScore != null) {
            return new Set(ObjectUtils.defaultIfNull(participantOneScore, (byte) 0), ObjectUtils
                    .defaultIfNull(participantTwoScore, (byte) 0));
        }
        return null;
    }
}
