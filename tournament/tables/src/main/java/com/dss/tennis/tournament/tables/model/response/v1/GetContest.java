package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.CONTEST;

@Deprecated
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GetContest<T extends GetParticipant> {

    {
        this.setType(CONTEST.value);
    }

    private Integer id;
    private String type;
    private Integer winner;
    private boolean techDefeat;
    private T participantOne;
    private T participantTwo;
    private GetScore score;
}
