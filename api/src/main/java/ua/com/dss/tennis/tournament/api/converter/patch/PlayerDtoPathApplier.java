package ua.com.dss.tennis.tournament.api.converter.patch;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerPatch;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;

import java.util.Optional;
import java.util.function.Consumer;

@Getter
@Setter
public class PlayerDtoPathApplier implements Converter<PlayerPatch, PlayerDTO> {

    @Override
    public PlayerDTO convert(MappingContext<PlayerPatch, PlayerDTO> context) {
        PlayerPatch patch = context.getSource();
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