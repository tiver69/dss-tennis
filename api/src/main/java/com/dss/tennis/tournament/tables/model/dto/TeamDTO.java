package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.validator.constraint.anotation.Required;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO extends AbstractSequentialDTO {

    private Integer id;

    @Required(message = "PLAYER_ONE_EMPTY")
    private PlayerDTO playerOne;

    @Required(message = "PLAYER_TWO_EMPTY")
    private PlayerDTO playerTwo;
}
