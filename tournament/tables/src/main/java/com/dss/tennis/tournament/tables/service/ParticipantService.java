package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.SuccessResponseDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.validator.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ParticipantService {

    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private PageableValidator pageableValidator;

    public PlayerDTO getPlayer(Integer playerId) {
        return playerHelper.getPlayer(playerId);
    }

    public SuccessResponseDTO<PageableDTO<PlayerDTO>> getPlayersPage(int page, byte pageSize) {
        Set<ErrorData> warnings = new HashSet<>();
        Pageable pageableRequestParameter = pageableValidator.validatePageableRequest(page, pageSize, warnings);

        PageableDTO<PlayerDTO> playersPage = playerHelper.getPlayersPage(pageableRequestParameter);
        Pageable newPageableRequestParameter = pageableValidator
                .validateUpperPage(pageableRequestParameter, playersPage.getTotalPages(), warnings);
        if (newPageableRequestParameter != null) {
            playersPage = playerHelper.getPlayersPage(newPageableRequestParameter);
        }

        return new SuccessResponseDTO<>(playersPage, warnings);
    }
}
