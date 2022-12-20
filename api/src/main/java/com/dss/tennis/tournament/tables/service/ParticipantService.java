package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.PatchApplierHelper;
import com.dss.tennis.tournament.tables.helper.participant.ParticipantHelper;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerPatch;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.validator.PageableValidator;
import com.dss.tennis.tournament.tables.validator.participant.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.participant.TeamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.PLAYER;

@Service
public class ParticipantService {

    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private TeamHelper teamHelper;
    @Autowired
    private PageableValidator pageableValidator;
    @Autowired
    private PlayerValidator playerValidator;
    @Autowired
    private TeamValidator teamValidator;
    @Autowired
    private PatchApplierHelper patchApplierHelper;

    @Transactional
    public PlayerDTO createNewPlayer(PlayerDTO playerDto) {
        Set<ErrorDataDTO> errorSet = playerValidator.validatePlayer(playerDto);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        Integer playerId = playerHelper.saveParticipant(playerDto);
        return playerHelper.getParticipantDto(playerId);
    }

    public TeamDTO createNewTeam(TeamDTO teamDto) {
        Set<ErrorDataDTO> errorSet = teamValidator.validateTeamCreation(teamDto);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        Integer teamId = teamHelper.saveParticipant(teamDto);
        return teamHelper.getParticipantDto(teamId);
    }

    public PlayerDTO updatePlayer(PlayerPatch patch, Integer playerId) {
        PlayerDTO player = playerHelper.getParticipantDto(playerId);
        PlayerDTO updatedPlayer = patchApplierHelper.applyPatch(patch, player);
        Set<ErrorDataDTO> errorSet = playerValidator.validatePlayer(updatedPlayer);
        if (!errorSet.isEmpty()) {
            throw new DetailedException(errorSet);
        }

        playerHelper.saveParticipant(updatedPlayer);
        return playerHelper.getParticipantDto(playerId);
    }

    public PlayerDTO getPlayerDTO(Integer playerId) {
        return playerHelper.getParticipantDto(playerId);
    }

    public TeamDTO getTeamDTO(Integer teamId) {
        return teamHelper.getParticipantDto(teamId);
    }

    public ResponseWarningDTO<PageableDTO> getParticipantPage(int page, byte pageSize,
                                                              ResourceObjectType participantType) {
        Set<ErrorDataDTO> warnings = new HashSet<>();
        ParticipantHelper helper = getParticipantHelper(participantType);
        Pageable pageableRequestParameter = pageableValidator
                .validatePageableRequest(page, pageSize, warnings, participantType);

        PageableDTO<?> participantPage = helper.getParticipantPage(pageableRequestParameter);
        Pageable newPageableRequestParameter = pageableValidator
                .validateUpperPage(pageableRequestParameter, participantPage
                        .getTotalPages(), warnings, participantType);
        if (newPageableRequestParameter != null) {
            participantPage = helper.getParticipantPage(newPageableRequestParameter);
        }

        return new ResponseWarningDTO<>(participantPage, warnings);
    }

    private ParticipantHelper<?, ?> getParticipantHelper(ResourceObjectType participantType) {
        return participantType == PLAYER ? playerHelper : teamHelper;
    }
}
