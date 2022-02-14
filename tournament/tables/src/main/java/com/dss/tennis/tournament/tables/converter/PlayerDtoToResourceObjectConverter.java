package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetPlayer;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@Getter
@Setter
public class PlayerDtoToResourceObjectConverter implements Converter<PlayerDTO, ResourceObject> {

    @Override
    public ResourceObject convert(MappingContext<PlayerDTO, ResourceObject> context) {
        PlayerDTO playerDTO = context.getSource();
        ResourceObject resourceObject = context.getDestination();

        GetPlayer playerAttributes = GetPlayer.builder()
                .firstName(playerDTO.getFirstName())
                .lastName(playerDTO.getLastName())
                .build();

        resourceObject.setType(ResourceObjectType.PLAYER.value);
        resourceObject.setAttributes(playerAttributes);
        return resourceObject;
    }
}