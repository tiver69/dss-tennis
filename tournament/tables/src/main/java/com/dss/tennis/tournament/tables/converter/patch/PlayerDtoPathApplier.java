package com.dss.tennis.tournament.tables.converter.patch;

import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.request.PatchPlayer;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Optional;
import java.util.function.Consumer;

@Getter
@Setter
public class PlayerDtoPathApplier implements Converter<PatchPlayer, PlayerDTO> {

    @Override
    public PlayerDTO convert(MappingContext<PatchPlayer, PlayerDTO> context) {
        PatchPlayer patch = context.getSource();
        PlayerDTO player = context.getDestination();

        applyValue(patch.getFirstName(), player::setFirstName);
        applyValue(patch.getLastName(), player::setLastName);
        applyValue(patch.getBirthDate(), player::setBirthDate);
        applyValue(patch.getExperienceYear(), player::setExperienceYear);
        applyValue(patch.getLeadingHand(), player::setLeadingHand);

        return player;
    }

    private <T> void applyValue(Optional<T> patchValue, Consumer<T> setFunction) {
            patchValue.ifPresent(setFunction);
    }
}