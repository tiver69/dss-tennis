package ua.com.dss.tennis.tournament.api.converter.request;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerPatch;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerRequest.PlayerRequestAttributes;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerRequest.UpdatePlayerRequest;

import java.util.Optional;

@Getter
@Setter
public class UpdatePlayerRequestToPatchPlayerConverter implements Converter<UpdatePlayerRequest, PlayerPatch> {

    @Override
    public PlayerPatch convert(MappingContext<UpdatePlayerRequest, PlayerPatch> context) {
        UpdatePlayerRequest update = context.getSource();
        PlayerRequestAttributes playerAttributes = update.getAttributes();

        PlayerPatch playerPatch = new PlayerPatch();
        playerPatch.setFirstName(Optional.ofNullable(playerAttributes.getFirstName()));
        playerPatch.setLastName(Optional.ofNullable(playerAttributes.getLastName()));
        playerPatch.setBirthDate(Optional.ofNullable(playerAttributes.getBirthDate()));
        playerPatch.setExperienceYear(Optional.ofNullable(playerAttributes.getExperienceYear()));
        playerPatch.setLeadingHand(Optional.ofNullable(playerAttributes.getLeadingHand()));
        return playerPatch;
    }
}