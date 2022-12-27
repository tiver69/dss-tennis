package ua.com.dss.tennis.tournament.api.model.definitions.contest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechDefeat {
    private Boolean participantOne;
    private Boolean participantTwo;
}
