package com.dss.tennis.tournament.tables.helper.participant;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.validator.participant.PlayerValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerHelper extends ParticipantHelper<Player, PlayerDTO> {

    @Autowired
    private PlayerValidator playerValidator;
    @Autowired
    private ConverterHelper converterHelper;

    @Override
    public Integer saveParticipant(PlayerDTO playerDto) {
        Player player = converterHelper.convert(playerDto, Player.class);
        Player savedPlayer = playerRepository.save(player);
        return savedPlayer.getId();
    }

    @Override
    public boolean isParticipantExist(Integer playerId) {
        return playerRepository.existsById(playerId);
    }

    @Override
    public boolean isParticipantNotExist(Integer playerId) {
        return !isParticipantExist(playerId);
    }

    public boolean isPlayerExist(PlayerDTO playerDTO) {
        if (StringUtils.isBlank(playerDTO.getFirstName()) || StringUtils.isBlank(playerDTO.getLastName()))
            return false;
        Optional<Player> player = playerRepository
                .findByFirstNameAndLastName(playerDTO.getFirstName(), playerDTO.getLastName());
        return player.isPresent() && player.get().getId() != playerDTO.getId() ;
    }

    @Override
    public Player getParticipant(Integer playerId) {
        Optional<Player> repositoryPlayer = playerRepository.findById(playerId);
        if (!repositoryPlayer.isPresent())
            throw new DetailedException(ErrorConstants.ErrorKey.PLAYER_NOT_FOUND, playerId);

        return repositoryPlayer.get();
    }

    @Override
    public PageableDTO<PlayerDTO> getParticipantPage(Pageable pageableRequestParameter) {
        Page<Player> playersPage = playerRepository.findAll(pageableRequestParameter);
        List<PlayerDTO> players = playersPage.getContent().stream()
                .map(player -> converterHelper.convert(player, PlayerDTO.class))
                .collect(Collectors.toList());

        return PageableDTO.<PlayerDTO>builder()
                .page(players)
                .currentPage(pageableRequestParameter.getPageNumber())
                .totalPages(playersPage.getTotalPages())
                .pageSize(pageableRequestParameter.getPageSize())
                .build();
    }

    @Override
    public PlayerDTO getParticipantDto(Integer playerId) {
        return converterHelper.convert(getParticipant(playerId), PlayerDTO.class);
    }

    @Override
    public List<Player> getTournamentParticipants(Integer tournamentId) {
        return playerRepository.findPlayersBySingleTournamentId(tournamentId);
    }

    @Override
    public List<PlayerDTO> getTournamentPlayerDTOs(Integer tournamentId) {
        List<Player> players = playerRepository.findPlayersBySingleTournamentId(tournamentId);
        return players.stream().map(player -> converterHelper.convert(player, PlayerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<Integer> getTournamentPlayerIds(Integer tournamentId) {
        List<Integer> playerIds =
                playerRepository.findPlayerIdsBySingleTournamentId(tournamentId);

        return playerIds.stream().allMatch(Objects::isNull) ? new ArrayList<>() : new ArrayList<>(playerIds);
    }

    @Override
    public List<Integer> getParticipantIdsForEnrolling(Integer tournamentId,
                                                       List<ResourceObjectDTO> newPlayers,
                                                       Set<ErrorDataDTO> warnings) {
        List<Integer> currentPlayerIds = getTournamentPlayerIds(tournamentId);
        List<Integer> participantIdsForEnrolling = new ArrayList<>();

        for (ResourceObjectDTO newPlayer : newPlayers) {
            ErrorDataDTO warning = playerValidator.validateParticipantForEnrolling(currentPlayerIds, newPlayer);
            if (warning != null) warnings.add(warning);
            else {
                currentPlayerIds.add(newPlayer.getId());
                participantIdsForEnrolling.add(newPlayer.getId());
            }
        }
        return participantIdsForEnrolling;
    }
}
