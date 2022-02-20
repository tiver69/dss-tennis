package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetPageablePlayers;
import com.dss.tennis.tournament.tables.model.response.v1.GetPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class PageableDtoToGetPageablePlayerConverter implements Converter<PageableDTO, GetPageablePlayers> {

    private ModelMapper modelMapper;

    @Override
    public GetPageablePlayers convert(MappingContext<PageableDTO, GetPageablePlayers> context) {
        PageableDTO<PlayerDTO> pageableDto = context.getSource();
        GetPageablePlayers pageablePlayers = context.getDestination();

        List<GetPlayer> players = pageableDto.getPage().stream()
                .map(player -> modelMapper.map(player, GetPlayer.class)).collect(Collectors.toList());
        pageablePlayers.setPlayers(players);
        return pageablePlayers;
    }
}