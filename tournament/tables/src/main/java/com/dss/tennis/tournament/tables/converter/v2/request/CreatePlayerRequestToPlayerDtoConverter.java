package com.dss.tennis.tournament.tables.converter.v2.request;

import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.CretePlayerRequest;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.PlayerRequestAttributes;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

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