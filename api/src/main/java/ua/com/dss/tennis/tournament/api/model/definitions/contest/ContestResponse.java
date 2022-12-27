package ua.com.dss.tennis.tournament.api.model.definitions.contest;

import lombok.*;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;

import java.util.List;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.CONTEST;

@Data
@AllArgsConstructor
public class ContestResponse {

    private ContestResponseData data;
    private List<Object> included;

    @Getter
    @Setter
    @Builder
    public static class ContestResponseData implements ua.com.dss.tennis.tournament.api.model.definitions.Data {
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
