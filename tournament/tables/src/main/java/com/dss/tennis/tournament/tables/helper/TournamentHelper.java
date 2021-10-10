package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TournamentHelper {

    @Autowired
    private TournamentRepository tournamentRepository;

    public Tournament createNewTournament(CreateTournamentDTO createTournamentDTO) {
        Tournament tournament = Tournament.builder().name(createTournamentDTO.getName())
                .type(createTournamentDTO.getType())
                .inProgress(true)
                .build();
        return tournamentRepository.save(tournament);
    }
}
