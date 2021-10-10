package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.db.v1.Contest;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Score;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContestHelper {

    @Autowired
    private ContestRepository contestRepository;

    public Contest createNewContest(Player playerOne, Player playerTwo, Tournament tournament) {
        Contest contest = Contest.builder()
                .playerOne(playerOne)
                .playerTwo(playerTwo)
                .tournament(tournament)
                .score(new Score())
                .build();
        return contestRepository.save(contest);
    }
}
