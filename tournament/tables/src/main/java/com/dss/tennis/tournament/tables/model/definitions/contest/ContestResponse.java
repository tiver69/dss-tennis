package com.dss.tennis.tournament.tables.model.definitions.contest;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import lombok.*;

import java.util.List;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.CONTEST;

@Data
@AllArgsConstructor
public class ContestResponse {

    private ContestResponseData data;
    private List<Object> included;

    @Getter
    @Setter
    @Builder
    public static class ContestResponseData {
        private int id;
        private final String type = CONTEST.value;
        private ContestAttributes attributes;
        private ContestRelationships relationships;
        private Links links;
    }

    @Getter
    @Setter
    @Builder
    public static class ContestRelationships {
        private SimpleResourceObject participantOne;
        private SimpleResourceObject participantTwo;
        private SimpleResourceObject winner;
    }
}
