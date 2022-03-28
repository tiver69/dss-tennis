package com.dss.tennis.tournament.tables.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchTechDefeat {
    private Boolean participantOne;
    private Boolean participantTwo;
}
