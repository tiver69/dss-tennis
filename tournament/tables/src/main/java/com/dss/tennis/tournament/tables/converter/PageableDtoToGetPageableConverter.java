package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetPageable;
import com.dss.tennis.tournament.tables.model.response.v1.GetPlayer;
import com.dss.tennis.tournament.tables.model.response.v1.GetTeam;
import com.dss.tennis.tournament.tables.model.response.v1.GetTournament;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class PageableDtoToGetPageableConverter<T, E> implements Converter<PageableDTO<T>, GetPageable<E>> {

    private Map<Class<?>, Class> pageableDtoClassTOResponseClass = new HashMap<>();
    private ModelMapper modelMapper;

    public PageableDtoToGetPageableConverter(ModelMapper modelMapper) {
        pageableDtoClassTOResponseClass.put(TeamDTO.class, GetTeam.class);
        pageableDtoClassTOResponseClass.put(PlayerDTO.class, GetPlayer.class);
        pageableDtoClassTOResponseClass.put(TournamentDTO.class, GetTournament.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public GetPageable<E> convert(MappingContext<PageableDTO<T>, GetPageable<E>> context) {
        PageableDTO<T> pageableDto = context.getSource();
        GetPageable<E> pageableData = context.getDestination();

        List<E> page = pageableDto.getPage().stream()
                .map(pageItem -> {
                    Class<E> className = pageableDtoClassTOResponseClass.get(pageItem.getClass());
                    return modelMapper.map(pageItem, className);
                })
                .collect(Collectors.toList());
        pageableData.setPage(page);
        return pageableData;
    }
}