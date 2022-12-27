package ua.com.dss.tennis.tournament.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.helper.PatchApplierHelper;
import ua.com.dss.tennis.tournament.api.helper.participant.ParticipantHelper;
import ua.com.dss.tennis.tournament.api.helper.participant.PlayerHelper;
import ua.com.dss.tennis.tournament.api.helper.participant.TeamHelper;
import ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerPatch;
import ua.com.dss.tennis.tournament.api.model.dto.*;
import ua.com.dss.tennis.tournament.api.validator.PageableValidator;
import ua.com.dss.tennis.tournament.api.validator.participant.PlayerValidator;
import ua.com.dss.tennis.tournament.api.validator.participant.TeamValidator;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.PLAYER;

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
