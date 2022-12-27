package ua.com.dss.tennis.tournament.api.model.definitions.player;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.com.dss.tennis.tournament.api.model.db.v1.LeadingHand;

@Data
@NoArgsConstructor
@SuperBuilder
public class PlayerAttributes {
    private String firstName;
    private String lastName;
    private LeadingHand leadingHand;
}
