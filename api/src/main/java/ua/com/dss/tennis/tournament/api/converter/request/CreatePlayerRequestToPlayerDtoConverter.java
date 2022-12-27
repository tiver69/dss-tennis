package ua.com.dss.tennis.tournament.api.converter.request;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerRequest.CretePlayerRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerRequest.PlayerRequestAttributes;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;

@Getter
@Setter
public class CreatePlayerRequestToPlayerDtoConverter implements Converter<CretePlayerRequest, PlayerDTO> {

    @Override
    public PlayerDTO convert(MappingContext<CretePlayerRequest, PlayerDTO> context) {
        CretePlayerRequest createPlayer = context.getSource();
        PlayerRequestAttributes playerAttributes = createPlayer.getAttributes();

        PlayerDTO playerDto = new PlayerDTO();
        playerDto.setFirstName(playerAttributes.getFirstName());
        playerDto.setLastName(playerAttributes.getLastName());
        playerDto.setBirthDate(playerAttributes.getBirthDate());
        playerDto.setExperienceYear(playerAttributes.getExperienceYear());
        playerDto.setLeadingHand(playerAttributes.getLeadingHand());
        return playerDto;
    }
}