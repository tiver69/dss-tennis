package com.dss.tennis.tournament.tables.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO extends AbstractSequentialDTO {

    private PlayerDTO playerOne;
    private PlayerDTO playerTwo;
}
