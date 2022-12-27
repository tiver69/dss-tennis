package ua.com.dss.tennis.tournament.api.model.definitions.player;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.PLAYER;

@Data
public class PlayerResponse {

    private PlayerResponseData data;

    public PlayerResponse(PlayerResponseData data) {
        this.data = data;
    }

    @Getter
    @Setter
    public static class PlayerResponseData implements ua.com.dss.tennis.tournament.api.model.definitions.Data {
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
