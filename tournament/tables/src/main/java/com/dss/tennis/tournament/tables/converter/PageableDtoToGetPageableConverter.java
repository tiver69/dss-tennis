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

    private Map<Class<?>, Class> pageableDtoClassToResponseClass = new HashMap<>();
    private ModelMapper modelMapper;

    public PageableDtoToGetPageableConverter(ModelMapper modelMapper) {
        pageableDtoClassToResponseClass.put(TeamDTO.class, GetTeam.class);
        pageableDtoClassToResponseClass.put(PlayerDTO.class, GetPlayer.class);
        pageableDtoClassToResponseClass.put(TournamentDTO.class, GetTournament.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public GetPageable<E> convert(MappingContext<PageableDTO<T>, GetPageable<E>> context) {
        PageableDTO<T> pageableDto = context.getSource();
        GetPageable<E> pageableData = context.getDestination();

        List<E> page = pageableDto.getPage().stream()
                .map(pageItem -> {
                    Class<E> className = pageableDtoClassToResponseClass.get(pageItem.getClass());
                    return modelMapper.map(pageItem, className);
                })
                .collect(Collectors.toList());
        pageableData.setPage(page);
        return pageableData;
    }
}