package com.dss.tennis.tournament.tables.model.definitions.tournament;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.Meta.CommonMeta;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TOURNAMENT;

@Data
@AllArgsConstructor
public class TournamentResponse {

    private CommonMeta meta;
    private TournamentResponseData data;
    private List<Object> included;

    @Getter
    @Setter
    @Builder
    public static class TournamentResponseData implements com.dss.tennis.tournament.tables.model.definitions.Data {
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
