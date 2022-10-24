package com.dss.tennis.tournament.tables.converter.v2.request;

import com.dss.tennis.tournament.tables.model.definitions.team.CreateTeamRequest;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamRelationships;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@Getter
@Setter
public class CreateTeamRequestToTeamDtoConverter implements Converter<CreateTeamRequest, TeamDTO> {

    @Override
    public TeamDTO convert(MappingContext<CreateTeamRequest, TeamDTO> context) {
        CreateTeamRequest createTeam = context.getSource();
        TeamRelationships teamRelationships = createTeam.getRelationships();

        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setPlayerOne(
                PlayerDTO.builder()
                        .id(teamRelationships.getPlayers().get(0).getId())
                        .build()
        );
        teamDTO.setPlayerTwo(
                PlayerDTO.builder()
                        .id(teamRelationships.getPlayers().get(1).getId())
                        .build()
        );
        return teamDTO;
    }
}