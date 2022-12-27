package ua.com.dss.tennis.tournament.api.helper.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.dss.tennis.tournament.api.helper.participant.TeamHelper;
import ua.com.dss.tennis.tournament.api.model.db.v1.Team;
import ua.com.dss.tennis.tournament.api.model.db.v2.Contest;
import ua.com.dss.tennis.tournament.api.model.db.v2.DoubleContest;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.DoubleContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TeamDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoundDoubleContestFactory extends RoundContestFactory {

    @Autowired
    private TeamHelper teamHelper;

    @Override
    public void createContestsForTournament(Integer tournamentId, List<Integer> newTeamIds) {
        List<Team> currentTeams = teamHelper.getTournamentParticipants(tournamentId);
        for (Integer newTeamId : newTeamIds) {
            Team newTeam = teamHelper.getParticipant(newTeamId);
            for (Team currentTeam : currentTeams)
                contestHelper.createNewDoubleContest(newTeam, currentTeam, tournamentId);
            currentTeams.add(newTeam);
        }
    }

    @Override
    public void removeParticipantFromTournament(Integer teamId, int tournamentId, boolean techDefeat) {
        if (techDefeat)
            contestHelper.getTournamentTeamContests(teamId, tournamentId).forEach(contestDTO -> contestHelper
                    .updateContestTechDefeatForParticipantRemoving(teamId, contestDTO));
        else
            removeContests(() -> contestRepository.findByTeamIdAndDoubleTournamentId(teamId, tournamentId));
    }

    @Override
    public ContestDTO getContestDTO(Integer contestId, Integer tournamentId) {
        DoubleContestDTO contest = (DoubleContestDTO) getBasicContestDTO(contestId, tournamentId);

        contest.setTeamOne(teamHelper.getParticipantDto(contest.getParticipantOneId()));
        contest.setTeamTwo(teamHelper.getParticipantDto(contest.getParticipantTwoId()));
        return contest;
    }

    @Override
    public Iterable<ContestDTO> getContestDTOsWithParticipants(Integer tournamentId) {
        Map<Integer, PlayerDTO> players = teamHelper.getTournamentPlayerDtoMap(tournamentId);
        Iterable<ContestDTO> contests = super.getContestDTOs(tournamentId);

        return StreamSupport.stream(contests.spliterator(), true).map(DoubleContestDTO.class::cast).peek(contest -> {
            TeamDTO teamOne = contest.getTeamOne();
            TeamDTO teamTwo = contest.getTeamTwo();

            teamOne.setPlayerOne(players.get(teamOne.getPlayerOne().getId()));
            teamOne.setPlayerTwo(players.get(teamOne.getPlayerTwo().getId()));
            teamTwo.setPlayerOne(players.get(teamTwo.getPlayerOne().getId()));
            teamTwo.setPlayerTwo(players.get(teamTwo.getPlayerTwo().getId()));
        }).collect(Collectors.toList());
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
