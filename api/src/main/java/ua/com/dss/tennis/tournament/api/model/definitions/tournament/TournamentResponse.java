package ua.com.dss.tennis.tournament.api.model.definitions.tournament;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.Meta.CommonMeta;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;

import java.util.List;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TOURNAMENT;

@Data
@AllArgsConstructor
public class TournamentResponse {

    private CommonMeta meta;
    private TournamentResponseData data;
    private List<Object> included;

    @Getter
    @Setter
    @Builder
    public static class TournamentResponseData implements ua.com.dss.tennis.tournament.api.model.definitions.Data {
        private int id;
        private final String type = TOURNAMENT.value;
        private TournamentResponseAttributes attributes;
        private TournamentRelationships relationships;
        private Links links;
    }

    @Getter
    @Setter
    @Builder
    public static class TournamentRelationships {
        private List<SimpleResourceObject> contests;
        private SimpleResourceObject finalContest;
    }


    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class TournamentResponseAttributes extends TournamentAttributes {
        private Byte participantsNumber;
        private Byte progress;
    }
}
