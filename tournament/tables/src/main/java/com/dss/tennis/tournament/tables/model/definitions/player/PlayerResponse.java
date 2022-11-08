package com.dss.tennis.tournament.tables.model.definitions.player;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.PLAYER;

@Data
public class PlayerResponse {

    private PlayerResponseData data;

    public PlayerResponse(PlayerResponseData data) {
        this.data = data;
    }

    @Getter
    @Setter
    public static class PlayerResponseData {
        private int id;
        private final String type = PLAYER.value;
        private PlayerResponseAttributes attributes;
        private Links links;
    }

    @Getter
    @Setter
    @SuperBuilder
    public static class PlayerResponseAttributes extends PlayerAttributes {
        private Integer age;
        private Integer experience;
    }
}
