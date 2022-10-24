package com.dss.tennis.tournament.tables.converter.v2.request;

import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.PlayerRequestAttributes;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.UpdatePlayerRequest;
import com.dss.tennis.tournament.tables.model.request.PatchPlayer;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Optional;

@Getter
@Setter
public class UpdatePlayerRequestToPatchPlayerConverter implements Converter<UpdatePlayerRequest, PatchPlayer> {

    @Override
    public PatchPlayer convert(MappingContext<UpdatePlayerRequest, PatchPlayer> context) {
        UpdatePlayerRequest update = context.getSource();
        PlayerRequestAttributes playerAttributes = update.getAttributes();

        PatchPlayer patchPlayer = new PatchPlayer();
        patchPlayer.setFirstName(Optional.ofNullable(playerAttributes.getFirstName()));
        patchPlayer.setLastName(Optional.ofNullable(playerAttributes.getLastName()));
        patchPlayer.setBirthDate(Optional.ofNullable(playerAttributes.getBirthDate()));
        patchPlayer.setExperienceYear(Optional.ofNullable(playerAttributes.getExperienceYear()));
        patchPlayer.setLeadingHand(Optional.ofNullable(playerAttributes.getLeadingHand()));
        return patchPlayer;
    }
}