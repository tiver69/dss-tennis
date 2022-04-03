package com.dss.tennis.tournament.tables.helper.participant;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import com.dss.tennis.tournament.tables.repository.TeamRepository;
import com.dss.tennis.tournament.tables.validator.participant.TeamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamHelper extends ParticipantHelper<Team, TeamDTO> {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamValidator teamValidator;

    @Override
    public Integer saveParticipant(TeamDTO teamDto) {
        Team team = converterHelper.convert(teamDto, Team.class);
        Team savedTeam = teamRepository.save(team);
        return savedTeam.getId();
    }

    @Override
    public boolean isParticipantExist(Integer teamId) {
        return teamRepository.findById(teamId).isPresent();
    }

    @Override
    public boolean isParticipantNotExist(Integer teamId) {
        return !isParticipantExist(teamId);
    }

    @Override
    public Team getParticipant(Integer teamId) {
        Optional<Team> repositoryTeam = teamRepository.findById(teamId);
        if (!repositoryTeam.isPresent())
            throw new DetailedException(ErrorConstants.TEAM_NOT_FOUND, teamId);

        return repositoryTeam.get();
    }

    @Override
    public TeamDTO getParticipantDto(Integer teamId) {
        Team team = getParticipant(teamId);
        TeamDTO teamDto = converterHelper.convert(team, TeamDTO.class);
        playerRepository.findById(team.getPlayerOneId())
                .ifPresent(player -> teamDto.setPlayerOne(converterHelper.convert(player, PlayerDTO.class)));
        playerRepository.findById(team.getPlayerTwoId())
                .ifPresent(player -> teamDto.setPlayerTwo(converterHelper.convert(player, PlayerDTO.class)));

        return teamDto;
    }

    @Override
    public List<Team> getTournamentParticipants(Integer tournamentId) {
        return teamRepository.findTeamsByDoubleTournamentId(tournamentId);
    }

    @Override
    public List<PlayerDTO> getTournamentPlayerDTOs(Integer tournamentId) {
        List<Player> players = playerRepository.findPlayersByDoubleTournamentId(tournamentId);
        return players.stream().map(player -> converterHelper.convert(player, PlayerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<Integer> getTournamentPlayerIds(Integer tournamentId) {
        List<Integer> participants =
                playerRepository.findPlayerIdsByDoubleTournamentId(tournamentId);

        return participants.stream().allMatch(Objects::isNull) ? new ArrayList<>() : new ArrayList<>(participants);
    }

    @Override
    public Set<Integer> getParticipantIdsForEnrolling(Integer tournamentId,
                                                      List<ResourceObjectDTO> newParticipants,
                                                      Set<ErrorDataDTO> warnings) {
        ArrayList<Integer> currentPlayerIds = getTournamentPlayerIds(tournamentId);
        Set<Integer> participantIdsForEnrolling = new HashSet<>();

        for (ResourceObjectDTO newTeam : newParticipants) {
            ErrorDataDTO warning = teamValidator.validateParticipantForEnrolling(currentPlayerIds, newTeam);
            if (warning == null) {
                Team team = getParticipant(newTeam.getId());
                currentPlayerIds.add(team.getPlayerOneId());
                currentPlayerIds.add(team.getPlayerTwoId());
                participantIdsForEnrolling.add(newTeam.getId());
            } else warnings.add(warning);
        }
        return participantIdsForEnrolling;
    }

    public ArrayList<Integer> getTournamentTeamIds(Integer tournamentId) {
        List<Integer> participants =
                teamRepository.findTeamIdsByDoubleTournamentId(tournamentId);

        return participants.stream().allMatch(Objects::isNull) ? new ArrayList<>() : new ArrayList<>(participants);
    }

    public boolean isTeamExist(Integer playerOneId, Integer playerTwoId) {
        return teamRepository.getTeamByPlayerIds(playerOneId, playerTwoId).isPresent();
    }
}
