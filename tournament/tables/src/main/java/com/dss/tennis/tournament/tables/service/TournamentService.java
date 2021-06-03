package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.model.v1.Player;
import com.dss.tennis.tournament.tables.model.v1.Tournament;
import com.dss.tennis.tournament.tables.model.v1.TournamentType;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import com.dss.tennis.tournament.tables.validator.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_TYPE_NOT_SUPPORTED;

@Service
public class TournamentService {

    @Autowired
    private TournamentValidator tournamentValidator;
    @Autowired
    private PlayerValidator playerValidator;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private TournamentHelper tournamentHelper;
    @Autowired
    private ContestHelper contestHelper;

    @Transactional
    //todo:refactor this method
    public void createNewTournament(CreateTournamentDTO createTournamentDTO) {
        validateCreateTournamentDTO(createTournamentDTO);

        List<Player> playerList = new ArrayList<>();
        createTournamentDTO.getPlayers().forEach(player -> {
            String firstName = player.split(" ")[0].trim();
            String lastName = player.split(" ")[1].trim();
            Optional<Player> repoPlayer = playerRepository.findByFirstNameAndLastName(firstName, lastName);
            if (!repoPlayer.isPresent()) {
                playerList.add(playerHelper.createNewPlayer(firstName, lastName));
            } else {
                playerList.add(repoPlayer.get());
            }
        });

        Tournament tournament = tournamentHelper.createNewTournament(createTournamentDTO);

        if (TournamentType.ROUND.equals(createTournamentDTO.getType())) {
            for (int i = 0; i < playerList.size(); i++) {
                for (int j = i + 1; j < playerList.size(); j++) {
                    contestHelper.createNewContest(playerList.get(i), playerList.get(j), tournament);
                }
            }
        } else {
            throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, createTournamentDTO.getType());
        }
    }

    private void validateCreateTournamentDTO(CreateTournamentDTO createTournamentDTO) {
        Set<DetailedErrorData> errorSet = new HashSet<>();
        tournamentValidator.validateTournamentName(createTournamentDTO.getName()).ifPresent(errorSet::add);
        //todo: add error details here with first/last name specifying
        createTournamentDTO.getPlayers().stream().map(playerValidator::validatePlayerName).filter(Optional::isPresent)
                .forEach(error -> errorSet.add(error.get()));
        if (!errorSet.isEmpty()) {
            throw new DetailedException(errorSet);
        }
    }

}
