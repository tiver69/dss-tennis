package com.dss.tennis.tournament.tables.model.definitions.player;

import com.dss.tennis.tournament.tables.model.db.v1.LeadingHand;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class PlayerAttributes {
    private String firstName;
    private String lastName;
    private LeadingHand leadingHand;
}
