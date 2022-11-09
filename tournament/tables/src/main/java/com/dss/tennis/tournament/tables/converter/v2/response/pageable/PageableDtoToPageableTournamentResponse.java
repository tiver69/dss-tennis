package com.dss.tennis.tournament.tables.converter.v2.response.pageable;

import com.dss.tennis.tournament.tables.model.definitions.tournament.PageableTournamentResponse;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class PageableDtoToPageableTournamentResponse extends AbstractPageableDtoToPageableResponse<TournamentDTO,
        PageableTournamentResponse> implements Converter<PageableDTO, PageableTournamentResponse> {

    public PageableDtoToPageableTournamentResponse(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public PageableTournamentResponse convert(MappingContext<PageableDTO, PageableTournamentResponse> context) {
        PageableDTO<TournamentDTO> pageableTournamentDto = context.getSource();
        return convertPageableResponse(pageableTournamentDto, PageableTournamentResponse.class);
    }
}

