package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseAttributes;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;
import java.time.Period;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.PLAYER;

@Getter
@Setter
public class PlayerDtoToPlayerResponseDataConverter implements Converter<PlayerDTO, PlayerResponseData> {

    @Override
    public PlayerResponseData convert(MappingContext<PlayerDTO, PlayerResponseData> context) {
        PlayerDTO playerDto = context.getSource();
        PlayerResponseData responseData = new PlayerResponseData();

        responseData.setAttributes(PlayerResponseAttributes.builder()
                .firstName(playerDto.getFirstName())
                .lastName(playerDto.getLastName())
                .age(countAge(playerDto.getBirthDate()))
                .experience(countExperience(playerDto.getExperienceYear()))
                .leadingHand(playerDto.getLeadingHand())
                .build());
        responseData.setId(playerDto.getId());
        responseData.setLinks(Links.builder()
                .self(String.format(PLAYER.selfLinkFormat, playerDto.getId()))
                .build()
        );
        return responseData;
    }

    private Integer countAge(LocalDate birthDate) {
        return birthDate != null ? Period.between(birthDate, LocalDate.now()).getYears() : null;
    }

    private Integer countExperience(Integer experienceStartYear) {
        return experienceStartYear != null ? LocalDate.now().getYear() - experienceStartYear : null;
    }
}