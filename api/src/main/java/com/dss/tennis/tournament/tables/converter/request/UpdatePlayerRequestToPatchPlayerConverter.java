package com.dss.tennis.tournament.tables.converter.request;

import com.dss.tennis.tournament.tables.model.definitions.player.PlayerPatch;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.PlayerRequestAttributes;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.UpdatePlayerRequest;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

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