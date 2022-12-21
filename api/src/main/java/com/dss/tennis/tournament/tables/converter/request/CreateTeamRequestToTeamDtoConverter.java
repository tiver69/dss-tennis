package com.dss.tennis.tournament.tables.converter.request;

import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.team.CreateTeamRequest;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.List;

@Getter
@Setter
public class CreateTeamRequestToTeamDtoConverter implements Converter<CreateTeamRequest, TeamDTO> {

    @Override
    public TeamDTO convert(MappingContext<CreateTeamRequest, TeamDTO> context) {
        CreateTeamRequest createTeam = context.getSource();
        if (createTeam.getRelationships() == null) return new TeamDTO();

        List<SimpleResourceObject> players = createTeam.getRelationships().getPlayers();
        PlayerDTO playerOne =
                players != null && players.size() > 0 && players.get(0).getId() != null ? new PlayerDTO(players.get(0)
                        .getId()) : null;
        PlayerDTO playerTwo =
                players != null && players.size() > 1 && players.get(1).getId() != null ? new PlayerDTO(players.get(1)
                        .getId()) : null;

        return TeamDTO.builder()
                .playerOne(playerOne)
                .playerTwo(playerTwo)
                .build();
    }
}