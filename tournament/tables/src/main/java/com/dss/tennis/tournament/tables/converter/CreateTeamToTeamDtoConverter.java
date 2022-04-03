package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import com.dss.tennis.tournament.tables.model.request.CreateTeam;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Set;

@Getter
@Setter
public class CreateTeamToTeamDtoConverter extends AbstractResourceObjectConverter implements Converter<CreateTeam,
        TeamDTO> {

    public CreateTeamToTeamDtoConverter(String allowedResourceType) {
        super(allowedResourceType);
    }

    @Override
    public TeamDTO convert(MappingContext<CreateTeam, TeamDTO> context) {
        CreateTeam createTeam = context.getSource();

        Set<ErrorDataDTO> errors = validateResourceObjectIdMappingWithId(createTeam.getPlayerOne(), "playerOne");
        errors.addAll(validateResourceObjectIdMappingWithId(createTeam.getPlayerTwo(), "playerTwo"));
        if (!errors.isEmpty()) throw new DetailedException(errors);
        return context.getDestination();
    }
}