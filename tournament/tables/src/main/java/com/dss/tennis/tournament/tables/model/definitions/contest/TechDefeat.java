package com.dss.tennis.tournament.tables.model.definitions.contest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechDefeat {
    private boolean participantOne;
    private boolean participantTwo;
}
