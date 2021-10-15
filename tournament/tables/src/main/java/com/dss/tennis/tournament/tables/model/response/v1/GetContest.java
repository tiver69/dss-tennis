package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetContest {

    private Integer id;
    private GetPlayer playerOne;
    private GetPlayer playerTwo;
    private GetScore score;
}
