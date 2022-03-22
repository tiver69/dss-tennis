package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetScore {

    private ResourceObject<GetSetScoreAttributes> setOne;
    private ResourceObject<GetSetScoreAttributes> setTwo;
    private ResourceObject<GetSetScoreAttributes> setThree;
    private ResourceObject<GetSetScoreAttributes> tieBreak;

    @Data
    @AllArgsConstructor
    public static class GetSetScoreAttributes {
        private byte participantOne;
        private byte participantTwo;
    }
}
