package ua.com.dss.tennis.tournament.api.model.dto;

import lombok.*;
import ua.com.dss.tennis.tournament.api.validator.constraint.anotation.Required;

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
