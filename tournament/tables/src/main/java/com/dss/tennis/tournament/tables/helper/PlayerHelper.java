package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerHelper {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ConverterHelper converterHelper;

    public PlayerDTO getPlayer(Integer playerId) {
        Optional<Player> repositoryPlayer = playerRepository.findById(playerId);
        if (!repositoryPlayer.isPresent())
            throw new DetailedException(ErrorConstants.PLAYER_NOT_FOUND, playerId);

        return converterHelper.convert(repositoryPlayer.get(), PlayerDTO.class);
    }

    public PageableDTO<PlayerDTO> getPlayersPage(Pageable pageableRequestParameter) {
        Page<Player> playersPage = playerRepository.findAll(pageableRequestParameter);
        List<PlayerDTO> players = playersPage.getContent().stream()
                .map(player -> converterHelper.convert(player, PlayerDTO.class))
                .collect(Collectors.toList());

        return PageableDTO.<PlayerDTO>builder().page(players)
                .currentPage(pageableRequestParameter.getPageNumber()).totalPages(playersPage.getTotalPages()).build();
    }

    public Player getPlayer(PlayerDTO playerDTO) {
        Optional<Player> repositoryPlayer = playerRepository
                .findByFirstNameAndLastName(playerDTO.getFirstName(), playerDTO.getLastName());
        if (repositoryPlayer.isPresent()) return repositoryPlayer.get();
        throw new DetailedException(ErrorConstants.PLAYER_NOT_FOUND_TOURNAMENT_CREATION, playerDTO.getSequenceNumber());
    }

    public boolean isPlayerNotExist(PlayerDTO playerDTO) {
        return !isPlayerExist(playerDTO);
    }

    public boolean isPlayerExist(PlayerDTO playerDTO) {
        if (StringUtils.isBlank(playerDTO.getFirstName()) || StringUtils.isBlank(playerDTO.getLastName()))
            return false;
        return playerRepository
                .findByFirstNameAndLastName(playerDTO.getFirstName(), playerDTO.getLastName()).isPresent();
    }

    public Player createNewPlayer(String firstName, String lastName) {
        Player player = Player.builder().firstName(firstName).lastName(lastName).build();
        return playerRepository.save(player);
    }


    /**
     * @return list of removed players
     */
    public List<PlayerDTO> removePlayerDuplicates(List<PlayerDTO> players) {
        List<PlayerDTO> duplicatePlayers = new ArrayList<>();
        for (int i = 0; i < players.size() - 1; i++) {
            PlayerDTO currentPlayer = players.get(i);
            for (int j = i + 1; j < players.size(); j++) {
                if (isSamePlayer(currentPlayer, players.get(j))) {
                    duplicatePlayers.add(players.get(j));
                }
            }
        }
        if (duplicatePlayers.size() != 0) {
            players.removeAll(duplicatePlayers);
        }
        return duplicatePlayers;
    }

    public boolean isSamePlayer(PlayerDTO playerOne, PlayerDTO playerTwo) {
        return StringUtils.equals(playerOne.getFirstName(), playerTwo.getFirstName()) && StringUtils
                .equals(playerOne.getLastName(), playerTwo.getLastName());
    }
}
