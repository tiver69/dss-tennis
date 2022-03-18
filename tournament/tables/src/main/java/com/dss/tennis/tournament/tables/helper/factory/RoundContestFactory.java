package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoundContestFactory implements AbstractContestFactory {

    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private TeamHelper teamHelper;
    @Autowired
    private ContestHelper contestHelper;
    @Autowired
    private ConverterHelper converterHelper;

    @Override
    public boolean createContestsForSingleTournament(Integer tournamentId, Set<Integer> newPlayerIds) {
        List<Integer> currentPlayerIds = playerHelper.getTournamentParticipants(tournamentId).stream()
                .map(Player::getId).collect(Collectors.toList());
        for (Integer newPlayerId : newPlayerIds) {
            for (Integer currentPlayerId : currentPlayerIds)
                contestHelper.createNewSingleContest(newPlayerId, currentPlayerId, tournamentId);
            currentPlayerIds.add(newPlayerId);
        }
        return currentPlayerIds.size() != 1;
    }

    @Override
    public boolean createContestsForDoubleTournament(Integer tournamentId, Set<Integer> newTeamIds) {
        List<Team> currentTeams = teamHelper.getTournamentParticipants(tournamentId);
        for (Integer newTeamId : newTeamIds) {
            Team newTeam = teamHelper.getParticipant(newTeamId);
            for (Team currentTeam : currentTeams)
                contestHelper.createNewDoubleContest(newTeam, currentTeam, tournamentId);
            currentTeams.add(newTeam);
        }
        return currentTeams.size() != 1;
    }

    @Override
    public List<ContestDTO> getContestDTOs(Integer tournamentId,
                                           Class<? extends ContestDTO> contestParticipantType) {
        List<Contest> contests = contestHelper.getTournamentContests(tournamentId);
        return contests.stream()
                .map(contest -> converterHelper.convert(contest, contestParticipantType)).collect(Collectors.toList());
    }
}
