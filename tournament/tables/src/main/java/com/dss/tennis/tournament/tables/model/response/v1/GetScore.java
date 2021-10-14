package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetScore {

    private Integer id;
    private Set setOne;
    private Set setTwo;
    private Set setThree;
    private Set tieBreak;

    @Data
    @AllArgsConstructor
    public static class Set {
        private byte playerOneScore;
        private byte playerTwoScore;
    }
}
