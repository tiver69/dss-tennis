package com.dss.tennis.tournament.tables.model.definitions.player;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

public class PlayerRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CretePlayerRequest {
        private final String type = ResourceObjectType.PLAYER.value;
        private PlayerRequestAttributes attributes;
    }

    @Getter
    @Setter
    public static class UpdatePlayerRequest extends CretePlayerRequest {
        private int id;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class PlayerRequestAttributes extends PlayerAttributes {
        private LocalDate birthDate;
        private Integer experienceYear;
    }
}
