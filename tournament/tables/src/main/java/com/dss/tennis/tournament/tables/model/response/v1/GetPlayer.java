package com.dss.tennis.tournament.tables.model.response.v1;

import com.dss.tennis.tournament.tables.model.db.v1.LeadingHand;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Deprecated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPlayer {

    {
        setType(ResourceObjectType.PLAYER.value);
    }

    private Integer id;
    private String type;
    private String firstName;
    private String lastName;

    private Integer age;
    private Integer experience;
    private LeadingHand leadingHand;
}
