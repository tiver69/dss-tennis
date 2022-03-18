package com.dss.tennis.tournament.tables.helper.participant;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.validator.participant.PlayerValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerHelper extends ParticipantHelper<Player> {

    @Autowired
    private PlayerValidator playerValidator;
    @Autowired
    private ConverterHelper converterHelper;

    @Override
    public boolean isParticipantExist(Integer playerId) {
        return playerRepository.findById(playerId).isPresent();
    }

    @Override
    public boolean isParticipantNotExist(Integer playerId) {
        return !isParticipantExist(playerId);
    }

    public boolean isPlayerExist(PlayerDTO playerDTO) {
        if (StringUtils.isBlank(playerDTO.getFirstName()) || StringUtils.isBlank(playerDTO.getLastName()))
            return false;
        return playerRepository
                .findByFirstNameAndLastName(playerDTO.getFirstName(), playerDTO.getLastName()).isPresent();
    }

    @Override
    public Player getParticipant(Integer playerId) {
        Optional<Player> repositoryPlayer = playerRepository.findById(playerId);
        if (!repositoryPlayer.isPresent())
            throw new DetailedException(ErrorConstants.PLAYER_NOT_FOUND, playerId);

        return repositoryPlayer.get();
    }

    public PlayerDTO getParticipantDto(Integer playerId) {
        return converterHelper.convert(getParticipant(playerId), PlayerDTO.class);
    }

    @Override
    public List<Player> getTournamentParticipants(Integer tournamentId) {
        return playerRepository.findPlayersByDoubleTournamentId(tournamentId);
    }

    @Override
    public List<PlayerDTO> getTournamentPlayerDTOs(Integer tournamentId) {
        List<Player> players = playerRepository.findPlayersBySingleTournamentId(tournamentId);
        return players.stream().map(player -> converterHelper.convert(player, PlayerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<Integer> getTournamentPlayerIds(Integer tournamentId) {
        List<Integer> participants =
                playerRepository.findPlayerIdsBySingleTournamentId(tournamentId);

        return participants.stream().allMatch(Objects::isNull) ? new ArrayList<>() : new ArrayList<>(participants);
    }

    @Override
    public Set<Integer> getParticipantIdsForEnrolling(Integer tournamentId,
                                                      List<ResourceObjectDTO> newPlayers,
                                                      Set<ErrorData> warnings) {
        List<Integer> currentPlayerIds = getTournamentPlayerIds(tournamentId);
        Set<Integer> participantIdsForEnrolling = new HashSet<>();

        for (ResourceObjectDTO newPlayer : newPlayers) {
            ErrorData warning = playerValidator.validateParticipantForEnrolling(currentPlayerIds, newPlayer);
            if (warning != null) warnings.add(warning);
            else {
                currentPlayerIds.add(newPlayer.getId());
                participantIdsForEnrolling.add(newPlayer.getId());
            }
        }
        return participantIdsForEnrolling;
    }

    public PageableDTO<PlayerDTO> getPlayersPage(Pageable pageableRequestParameter) {
        Page<Player> playersPage = playerRepository.findAll(pageableRequestParameter);
        List<PlayerDTO> players = playersPage.getContent().stream()
                .map(player -> converterHelper.convert(player, PlayerDTO.class))
                .collect(Collectors.toList());

        return PageableDTO.<PlayerDTO>builder().page(players)
                .currentPage(pageableRequestParameter.getPageNumber()).totalPages(playersPage.getTotalPages()).build();
    }

    public Player createNewPlayer(PlayerDTO playerDto) {
        return createNewPlayer(playerDto.getFirstName(), playerDto.getLastName());
    }

    public Player createNewPlayer(String firstName, String lastName) {
        Player player = Player.builder().firstName(firstName).lastName(lastName).build();
        return playerRepository.save(player);
    }
}
