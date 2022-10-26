package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.tournament.PageableTournamentResponse;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseAttributes;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseData;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.PLAYER;
import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.TOURNAMENT;

@Getter
@Setter
public class PageableDtoToPageableTournamentResponse implements Converter<PageableDTO, PageableTournamentResponse> {

    private ModelMapper modelMapper;

    public PageableDtoToPageableTournamentResponse(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PageableTournamentResponse convert(MappingContext<PageableDTO, PageableTournamentResponse> context) {
        PageableDTO<TournamentDTO> pageableTournamentDto = context.getSource();
        int pageSize = pageableTournamentDto.getPageSize();
        int currentPage = pageableTournamentDto.getCurrentPage();

        return PageableTournamentResponse.builder()
                .totalPages(pageableTournamentDto.getTotalPages())
                .page(pageableTournamentDto.getPage().stream()
                        .map(this::convertToTournamentResponseData)
                        .collect(Collectors.toList()))
                .links(Links.builder()
                        .first(String.format(PLAYER.pageableLinkFormat, 1, pageSize))
                        .last(String.format(PLAYER.pageableLinkFormat, pageableTournamentDto.getTotalPages(), pageSize))
                        .prev(String
                                .format(PLAYER.pageableLinkFormat, currentPage - 1, pageSize))
                        .self(String.format(PLAYER.pageableLinkFormat, currentPage, pageSize))
                        .next(String
                                .format(PLAYER.pageableLinkFormat, currentPage + 1, pageSize)) //todo: ignore next if
                        // current is last
                        .build())
                .build();
    }

    private TournamentResponseData convertToTournamentResponseData(TournamentDTO tournamentDto) {
        return TournamentResponseData.builder()
                .id(tournamentDto.getId())
                .attributes(convertTournamentAttributes(tournamentDto))
                .links(Links.builder()
                        .self(String.format(TOURNAMENT.selfLinkFormat, tournamentDto.getId()))
                        .build())
                .build();
    }

    private TournamentResponseAttributes convertTournamentAttributes(TournamentDTO tournament) {
        return TournamentResponseAttributes.builder()
                .name(tournament.getName())
                .tournamentType(tournament.getTournamentType())
                .participantType(tournament.getParticipantType())
                .status(tournament.getStatus())
                .beginningDate(tournament.getBeginningDate())
                .participantsNumber((byte) 100) //todo count participants
                .progress((byte) 100) //todo count player contests
                .build();
    }
}

