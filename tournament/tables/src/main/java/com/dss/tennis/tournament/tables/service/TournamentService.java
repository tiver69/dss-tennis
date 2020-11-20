package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.v1.Contest;
import com.dss.tennis.tournament.tables.model.v1.Player;
import com.dss.tennis.tournament.tables.model.v1.Score;
import com.dss.tennis.tournament.tables.model.v1.Tournament;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import com.dss.tennis.tournament.tables.validator.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class TournamentService {

    @Autowired
    private TournamentValidator tournamentValidator;
    @Autowired
    private PlayerValidator playerValidator;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private ContestRepository contestRepository;

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
                playerList.add(playerRepository.save(Player.builder().firstName(firstName).lastName(lastName).build()));
            } else {
                playerList.add(repoPlayer.get());
            }
        });

        Tournament tournament = tournamentRepository.save(Tournament.builder().name(createTournamentDTO.getName())
                .type(createTournamentDTO.getType())
                .inProgress(true)
                .build());

        for (int i = 0; i < playerList.size(); i++) {
            for (int j = i + 1; j < playerList.size(); j++) {
                Contest contest = Contest.builder().playerOne(playerList.get(i)).playerTwo(playerList.get(j))
                        .tournament(tournament).score(new Score()).build();
                contestRepository.save(contest);
            }
        }
    }

    private void validateCreateTournamentDTO(CreateTournamentDTO createTournamentDTO) {
        Set<String> errorSet = new HashSet<>();
        tournamentValidator.validateTournamentName(createTournamentDTO.getName()).ifPresent(errorSet::add);
        //todo: add error details here with name specifying
        createTournamentDTO.getPlayers().stream().map(playerValidator::validatePlayerName).filter(Optional::isPresent)
                .forEach(error -> errorSet.add(error.get()));
        if (!errorSet.isEmpty()) {
            //todo: error to return in response
            throw new IllegalArgumentException("Validation fail for tournament creation.");
        }
    }

}
