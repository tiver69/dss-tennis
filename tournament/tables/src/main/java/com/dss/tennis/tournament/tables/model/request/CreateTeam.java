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
public class CreateTeam {

    private ResourceObject playerOne;
    private ResourceObject playerTwo;
}
