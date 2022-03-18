package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.db.v1.Score;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.DoubleContest;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import com.dss.tennis.tournament.tables.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestHelper {

    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private TeamRepository teamRepository;

    public List<Contest> getTournamentContests(Integer tournamentId) {
        return contestRepository.findByTournamentId(tournamentId);
    }

    public Contest createNewSingleContest(Integer playerOneId, Integer playerTwoId, Integer tournamentId) {
        Contest contest = SingleContest.builder()
                .playerOneId(playerOneId)
                .playerTwoId(playerTwoId)
                .tournamentId(tournamentId)
                .score(new Score())
                .build();

        return contestRepository.save(contest);
    }

    public Contest createNewDoubleContest(Team teamOne, Team teamTwo, Integer tournamentId) {
        Contest contest = DoubleContest.builder()
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .tournamentId(tournamentId)
                .score(new Score())
                .build();

        return contestRepository.save(contest);
    }
}
