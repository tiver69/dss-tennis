package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.DoubleContest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.DoubleContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoundDoubleContestFactory extends RoundContestFactory {

    @Autowired
    private TeamHelper teamHelper;

    @Override
    public void createContestsForTournament(Integer tournamentId, List<Integer> newTeamIds, boolean shouldCreateScore) {
        List<Team> currentTeams = teamHelper.getTournamentParticipants(tournamentId);
        for (Integer newTeamId : newTeamIds) {
            Team newTeam = teamHelper.getParticipant(newTeamId);
            for (Team currentTeam : currentTeams)
                contestHelper.createNewDoubleContest(newTeam, currentTeam, tournamentId, shouldCreateScore);
            currentTeams.add(newTeam);
        }
    }

    @Override
    public void removeParticipantFromTournament(Integer teamId, int tournamentId, boolean techDefeat) {
        if (techDefeat)
            contestHelper.getTournamentTeamContests(teamId, tournamentId).forEach(contestDTO -> contestHelper
                    .updateDoubleContestTechDefeatForTeamRemoving(teamId, contestDTO));
        else
            removeContests(() -> contestRepository.findByTeamIdAndDoubleTournamentId(teamId, tournamentId));
    }

    @Override
    public Iterable<ContestDTO> getContestDTOs(Integer tournamentId, Map<Integer, PlayerDTO> players) {
        Iterable<ContestDTO> contests = super.getContestDTOs(tournamentId);
        contests.forEach(contest -> {
            TeamDTO teamOne = ((DoubleContestDTO) contest).getTeamOne();
            teamOne.setPlayerOne(players.get(teamOne.getPlayerOne().getId()));
            teamOne.setPlayerTwo(players.get(teamOne.getPlayerTwo().getId()));
            TeamDTO teamTwo = ((DoubleContestDTO) contest).getTeamTwo();
            teamTwo.setPlayerOne(players.get(teamTwo.getPlayerOne().getId()));
            teamTwo.setPlayerTwo(players.get(teamTwo.getPlayerTwo().getId()));
        });
        return contests;
    }

    @Override
    public Class<? extends ContestDTO> getContestParticipantDtoClass() {
        return DoubleContestDTO.class;
    }

    @Override
    public Class<? extends Contest> getContestParticipantClass() {
        return DoubleContest.class;
    }
}
