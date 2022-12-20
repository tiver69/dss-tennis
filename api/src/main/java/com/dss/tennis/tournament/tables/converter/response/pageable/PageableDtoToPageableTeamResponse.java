package com.dss.tennis.tournament.tables.converter.response.pageable;

import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.dss.tennis.tournament.tables.model.definitions.team.PageableTeamResponse;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PageableDtoToPageableTeamResponse extends AbstractPageableDtoToPageableResponse<TeamDTO,
        PageableTeamResponse> implements Converter<PageableDTO, PageableTeamResponse> {

    public PageableDtoToPageableTeamResponse(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public PageableTeamResponse convert(MappingContext<PageableDTO, PageableTeamResponse> context) {
        PageableDTO<TeamDTO> pageableTeamsDto = context.getSource();
        PageableTeamResponse response = convertPageableResponse(pageableTeamsDto, PageableTeamResponse.class);

        List<TeamDTO> page = pageableTeamsDto.getPage();
        List<Object> responseIncluded = page.stream()
                .flatMap(teamDto -> Stream.of(teamDto.getPlayerOne(), teamDto.getPlayerTwo()))
                .distinct()
                .map(playerDto -> getModelMapper().map(playerDto, PlayerResponseData.class))
                .collect(Collectors.toList());
        response.setIncluded(responseIncluded);

        return response;
    }
}