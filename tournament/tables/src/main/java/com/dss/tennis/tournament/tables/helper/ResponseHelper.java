package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.Meta;
import com.dss.tennis.tournament.tables.model.definitions.PageableResponse;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.dss.tennis.tournament.tables.model.definitions.team.PageableTeamResponse;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseData;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorResponse.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.GetTournament;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import com.dss.tennis.tournament.tables.model.response.v1.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ResponseHelper {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private WarningHandler warningHandler;

    public TeamResponse createTeamResponse(TeamDTO teamDto) {
        TeamResponseData responseData = converterHelper.convert(teamDto, TeamResponseData.class);

        List<Object> responseIncluded = new ArrayList<>();
        responseIncluded.add(converterHelper.convert(teamDto.getPlayerOne(), PlayerResponseData.class));
        responseIncluded.add(converterHelper.convert(teamDto.getPlayerTwo(), PlayerResponseData.class));

        return new TeamResponse(responseData, responseIncluded);
    }

    public PageableTeamResponse createPageableTeamResponse(ResponseWarningDTO<PageableDTO> source) {

        PageableTeamResponse response = (PageableTeamResponse) createPageableResponse(source,
                PageableTeamResponse.class);

        List<TeamDTO> page = source.getData().getPage();
        List<Object> responseIncluded = page.stream()
                .flatMap(teamDto -> Stream.of(teamDto.getPlayerOne(), teamDto.getPlayerTwo()))
                .distinct()
                .map(playerDto -> converterHelper.convert(playerDto, PlayerResponseData.class))
                .collect(Collectors.toList());
        response.setIncluded(responseIncluded);
        return response;
    }

    public <D extends PageableResponse> PageableResponse createPageableResponse(ResponseWarningDTO<PageableDTO> source,
                                                                                Class<D> destinationClass) {
        Set<ErrorDataDTO> warningsDto = source.getWarnings();
        Set<ErrorData> responseWarnings = warningsDto == null || warningsDto.isEmpty() ? null :
                warningsDto.stream().map(warningHandler::createErrorData).collect(Collectors.toSet());

        try {
            D response = destinationClass.getConstructor().newInstance();
            List responseData = (List)
                    source.getData().getPage().stream()
                            .map(pageItem -> converterHelper.convert(pageItem, response.getResponseDataClass()))
                            .collect(Collectors.toList());

            response.setMeta(Meta.builder().totalPages(source.getData().getTotalPages())
                    .currentPage(source.getData().getCurrentPage() + 1).warnings(responseWarnings).build());
            response.setData(responseData);
            response.setLinks(convertPageableLinks(source.getData().getTotalPages(), source.getData()
                    .getPageSize(), source.getData().getCurrentPage() + 1, response.getResponseDataType()));

            return response;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new DetailedException(ErrorConstants.INTERNAL_SERVER_ERROR, "Failed to create response object");
        }
    }

    private Links convertPageableLinks(int totalPages, int pageSize, int currentPage, ResourceObjectType type) {
        return Links.builder()
                .first(String.format(type.pageableLinkFormat, 1, pageSize))
                .last(String.format(type.pageableLinkFormat, totalPages, pageSize))
                .prev(currentPage != 1 ?
                        String.format(type.pageableLinkFormat, currentPage - 1, pageSize) : null)
                .self(String.format(type.pageableLinkFormat, currentPage, pageSize))
                .next(currentPage != totalPages ?
                        String.format(type.pageableLinkFormat, currentPage + 1, pageSize) : null)
                .build();
    }

    public <S, D> SuccessResponse<D> createSuccessResponse(SuccessResponseDTO<S> data, Class<D> responseClass) {
        return createSuccessResponse(data.getData(), null, data.getWarnings(), responseClass);
    }

    public SuccessResponse<GetTournament> createSuccessResponse(TournamentDTO tournamentDTO,
                                                                Set<ErrorDataDTO> warnings) {
        Set<ResourceObject> included = null;
        if (tournamentDTO.getPlayers() != null) {
            included = ((List<PlayerDTO>) tournamentDTO.getPlayers()).stream()
                    .map(player -> converterHelper.convert(player, ResourceObject.class))
                    .collect(Collectors.toSet());
        }
        return createSuccessResponse(tournamentDTO, included, warnings, GetTournament.class);
    }

    public <S, D> SuccessResponse<D> createSuccessResponse(S data, Class<D> responseClass) {
        return createSuccessResponse(data, null, null, responseClass);
    }

    public <S, D> SuccessResponse<D> createSuccessResponse(S data, Set<ResourceObject> included,
                                                           Set<ErrorDataDTO> warnings, Class<D> responseClass) {
        SuccessResponse<D> successResponse = new SuccessResponse<>();
        successResponse.setData(converterHelper.convert(data, responseClass));
        successResponse.setIncluded((included == null || included.isEmpty()) ? null : included);
        Set<ErrorData> warningsResponse = warnings == null || warnings.isEmpty() ? null :
                warnings.stream().map(warningHandler::createErrorData).collect(Collectors.toSet());
        successResponse.setWarnings(warningsResponse);

        return successResponse;
    }
}
