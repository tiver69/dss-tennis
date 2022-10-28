package com.dss.tennis.tournament.tables.model.definitions.contest;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ContestResponse {

    private ContestResponseData data;

    public ContestResponse(ContestResponseData data) {
        this.data = data;
    }

    @Getter
    @Setter
    @Builder
    public static class ContestResponseData {
        private int id;
        private final String type = ResourceObjectType.CONTEST.value;
        private ContestAttributes attributes;
        private ContestRelationships relationships;
        private List<Object> included;
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
