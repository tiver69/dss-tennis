package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.SuccessResponseDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.GetTournament;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject;
import com.dss.tennis.tournament.tables.model.response.v1.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResponseHelper {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private WarningHandler warningHandler;

    public <S, D> SuccessResponse<D> createSuccessResponse(SuccessResponseDTO<S> data, Class<D> responseClass) {
        return createSuccessResponse(data.getData(), null, data.getWarnings(), responseClass);
    }

    public SuccessResponse<GetTournament> createSuccessResponse(TournamentDTO tournamentDTO,
                                                                Set<ErrorDataDTO> warnings) {
        Set<ResourceObject> included = null;
        if (tournamentDTO.getPlayers() != null) {
            included = tournamentDTO.getPlayers().stream()
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
