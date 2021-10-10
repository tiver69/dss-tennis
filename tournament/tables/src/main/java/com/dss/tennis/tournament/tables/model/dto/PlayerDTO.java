package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.validator.anotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO extends AbstractSequentialDTO {

    @Required(message = "PLAYER_FIRST_NAME_EMPTY")
    private String firstName;
    @Required(message = "PLAYER_LAST_NAME_EMPTY")
    private String lastName;
}
