package com.dss.tennis.tournament.tables.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO extends AbstractSequentialDTO{

    private String firstName;
    private String lastName;
}
