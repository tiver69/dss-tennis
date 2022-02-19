package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetDoubleContest extends GetContest {

    private GetTeam teamOne;
    private GetTeam teamTwo;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class GetTeam {
        private Integer id;
        private Integer playerOneId;
        private Integer playerTwoId;
    }
}
