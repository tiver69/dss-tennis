package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetPlayer;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class PlayerDtoToGetPlayerConverter implements Converter<PlayerDTO, GetPlayer> {

    @Override
    public GetPlayer convert(MappingContext<PlayerDTO, GetPlayer> context) {
        PlayerDTO playerDTO = context.getSource();
        GetPlayer resourceObject = context.getDestination();

        resourceObject.setAge(countAge(playerDTO.getBirthDate()));
        resourceObject.setExperience(countExperience(playerDTO.getExperienceYear()));
        return resourceObject;
    }

    private Integer countAge(LocalDate birthDate) {
        return birthDate != null ? Period.between(birthDate, LocalDate.now()).getYears() : null;
    }

    private Integer countExperience(Integer experienceStartYear) {
        return experienceStartYear != null ? LocalDate.now().getYear() - experienceStartYear : null;
    }
}