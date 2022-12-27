package ua.com.dss.tennis.tournament.api.model.definitions.player;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.PLAYER;

public class PlayerRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CretePlayerRequest {
        private final String type = PLAYER.value;
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
