package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Score;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_HAS_NO_CONTESTS;

@Service
public class ContestHelper {

    @Autowired
    private ContestRepository contestRepository;

    public List<Contest> getTournamentContests(Integer tournamentId) {
        List<Contest> contests = contestRepository.findByTournamentId(tournamentId);
        if (contests.isEmpty()) throw new DetailedException(TOURNAMENT_HAS_NO_CONTESTS, tournamentId.toString());
        return contests;
    }

    public Contest createNewContest(Player playerOne, Player playerTwo, Tournament tournament) {
        Contest contest = SingleContest.builder()
                .playerOne(playerOne)
                .playerTwo(playerTwo)
                .tournament(tournament)
                .score(new Score())
                .build();
        return contestRepository.save(contest);
    }
}
