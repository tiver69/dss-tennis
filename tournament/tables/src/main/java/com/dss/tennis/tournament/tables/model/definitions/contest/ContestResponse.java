package com.dss.tennis.tournament.tables.model.definitions.contest;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.*;

import java.util.List;

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
        private final String type = ResourceObjectType.CONTEST.value;
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
