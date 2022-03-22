package com.dss.tennis.tournament.tables.model.request;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateScore {

    private ResourceObject<SetScoreAttributes> setOne;
    private ResourceObject<SetScoreAttributes> setTwo;
    private ResourceObject<SetScoreAttributes> setThree;
    private ResourceObject<SetScoreAttributes> tieBreak;

    @Data
    @AllArgsConstructor
    public static class SetScoreAttributes {
        private Byte participantOne;
        private Byte participantTwo;
    }
}

