package com.dss.tennis.tournament.tables.helper.participant;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.repository.TeamRepository;
import com.dss.tennis.tournament.tables.validator.participant.TeamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return teamRepository.existsById(teamId);
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
    public PageableDTO<TeamDTO> getParticipantPage(Pageable pageableRequestParameter) {
        Page<Team> teamsPage = teamRepository.findAll(pageableRequestParameter);
        List<TeamDTO> teams = teamsPage.getContent().stream()
                .map(this::convertTeamDto).collect(Collectors.toList());

        return PageableDTO.<TeamDTO>builder().page(teams)
                .currentPage(pageableRequestParameter.getPageNumber()).totalPages(teamsPage.getTotalPages()).build();
    }

    @Override
    public TeamDTO getParticipantDto(Integer teamId) {
        Team team = getParticipant(teamId);
        return convertTeamDto(team);
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
        List<Integer> playerIds =
                playerRepository.findPlayerIdsByDoubleTournamentId(tournamentId);

        return playerIds.stream().allMatch(Objects::isNull) ? new ArrayList<>() : new ArrayList<>(playerIds);
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
        List<Integer> teamIds =
                teamRepository.findTeamIdsByDoubleTournamentId(tournamentId);

        return teamIds.stream().allMatch(Objects::isNull) ? new ArrayList<>() : new ArrayList<>(teamIds);
    }

    public boolean isTeamExist(Integer playerOneId, Integer playerTwoId) {
        return teamRepository.getTeamByPlayerIds(playerOneId, playerTwoId).isPresent();
    }

    private TeamDTO convertTeamDto(Team team) {
        TeamDTO teamDto = converterHelper.convert(team, TeamDTO.class);
        playerRepository.findById(team.getPlayerOneId())
                .ifPresent(player -> teamDto.setPlayerOne(converterHelper.convert(player, PlayerDTO.class)));
        playerRepository.findById(team.getPlayerTwoId())
                .ifPresent(player -> teamDto.setPlayerTwo(converterHelper.convert(player, PlayerDTO.class)));

        return teamDto;
    }
}
