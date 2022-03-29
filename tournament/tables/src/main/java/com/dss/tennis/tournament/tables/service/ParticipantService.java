package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.PatchApplierHelper;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.SuccessResponseDTO;
import com.dss.tennis.tournament.tables.model.request.PatchPlayer;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import com.dss.tennis.tournament.tables.validator.PageableValidator;
import com.dss.tennis.tournament.tables.validator.participant.PlayerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class ParticipantService {

    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private PageableValidator pageableValidator;
    @Autowired
    private PlayerValidator playerValidator;
    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private PatchApplierHelper patchApplierHelper;

    @Transactional
    public PlayerDTO createNewPlayer(PlayerDTO playerDto) {
        Set<ErrorDataDTO> errorSet = playerValidator.validatePlayer(playerDto);
        if (!errorSet.isEmpty()) {
            throw new DetailedException(errorSet);
        }

        Integer playerId = playerHelper.savePlayer(playerDto);
        return playerHelper.getParticipantDto(playerId);
    }

    public PlayerDTO updatePlayer(PatchPlayer patch, Integer playerId) {
        PlayerDTO player = playerHelper.getParticipantDto(playerId);
        PlayerDTO updatedPlayer = patchApplierHelper.applyPatch(patch, player);
        Set<ErrorDataDTO> errorSet = playerValidator.validatePlayer(updatedPlayer);
        if (!errorSet.isEmpty()) {
            throw new DetailedException(errorSet);
        }

        playerHelper.savePlayer(updatedPlayer);
        return playerHelper.getParticipantDto(playerId);
    }

    public PlayerDTO getPlayerDTO(Integer playerId) {
        return playerHelper.getParticipantDto(playerId);
    }

    public SuccessResponseDTO<PageableDTO<PlayerDTO>> getPlayersPage(int page, byte pageSize) {
        Set<ErrorDataDTO> warnings = new HashSet<>();
        Pageable pageableRequestParameter = pageableValidator.validatePageableRequest(page, pageSize, warnings,
                ResourceObjectType.PLAYER);

        PageableDTO<PlayerDTO> playersPage = playerHelper.getPlayersPage(pageableRequestParameter);
        Pageable newPageableRequestParameter = pageableValidator
                .validateUpperPage(pageableRequestParameter, playersPage
                        .getTotalPages(), warnings, ResourceObjectType.PLAYER);
        if (newPageableRequestParameter != null) {
            playersPage = playerHelper.getPlayersPage(newPageableRequestParameter);
        }

        return new SuccessResponseDTO<>(playersPage, warnings);
    }
}
