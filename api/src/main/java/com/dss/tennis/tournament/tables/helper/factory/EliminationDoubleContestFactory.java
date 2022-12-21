package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.DoubleContest;
import com.dss.tennis.tournament.tables.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.StreamSupport;

@Service
public class EliminationDoubleContestFactory extends EliminationContestFactory {

    @Autowired
    private TeamHelper teamHelper;

    @Override
    public Integer createFirstLineEliminationContest(Integer firstParticipantId, Integer secondParticipantId,
                                                     Integer tournamentId) {
        Team firstTeam = teamHelper.getParticipant(firstParticipantId);
        Team secondTeam = teamHelper.getParticipant(secondParticipantId);
        return contestHelper.createNewDoubleContest(firstTeam, secondTeam, tournamentId).getId();
    }

    protected ContestDTO convertEliminationContestToBase(EliminationContestDTO eliminationContest) {
        TeamDTO firstParticipant = getEliminationContestParticipantFromParent(eliminationContest
                .getFirstParentContestDto());
        TeamDTO secondParticipant = getEliminationContestParticipantFromParent(eliminationContest
                .getSecondParentContestDto());
        DoubleContestDTO contestDTO = converterHelper.convert(eliminationContest, DoubleContestDTO.class);
        contestDTO.setTeamOne(firstParticipant);
        contestDTO.setTeamTwo(secondParticipant);
        return contestDTO;
    }

    private TeamDTO getEliminationContestParticipantFromParent(ContestDTO parentContestDTO) {
        Integer winnerId = parentContestDTO.getWinnerId();
        if (parentContestDTO instanceof DoubleContestDTO) {
            if (((DoubleContestDTO) parentContestDTO).getTeamOne().getId().equals(winnerId))
                return ((DoubleContestDTO) parentContestDTO).getTeamOne();
            if (((DoubleContestDTO) parentContestDTO).getTeamTwo().getId().equals(winnerId))
                return ((DoubleContestDTO) parentContestDTO).getTeamTwo();
        }

        EliminationContestDTO parentEliminationContest = (EliminationContestDTO) parentContestDTO;
        if (winnerId.equals(parentEliminationContest.getFirstParentContestDto().getWinnerId()))
            return getEliminationContestParticipantFromParent(parentEliminationContest.getFirstParentContestDto());
        if (winnerId.equals(parentEliminationContest.getSecondParentContestDto().getWinnerId()))
            return getEliminationContestParticipantFromParent(parentEliminationContest.getSecondParentContestDto());
        return null;
    }

    @Override
    public ContestDTO getContestDTO(Integer contestId, Integer tournamentId) {
        ContestDTO contestDto = getBasicContestDTO(contestId, tournamentId);
        if (contestDto instanceof EliminationContestDTO) {
            ((EliminationContestDTO) contestDto).forEach(parentContestDTO -> {
                if (parentContestDTO instanceof DoubleContestDTO) {
                    ((DoubleContestDTO) parentContestDTO)
                            .setTeamOne(teamHelper.getParticipantDto(parentContestDTO.getParticipantOneId()));
                    ((DoubleContestDTO) parentContestDTO)
                            .setTeamTwo(teamHelper.getParticipantDto(parentContestDTO.getParticipantTwoId()));
                }
            });
        } else {
            ((DoubleContestDTO) contestDto).setTeamOne(teamHelper.getParticipantDto(contestDto.getParticipantOneId()));
            ((DoubleContestDTO) contestDto).setTeamTwo(teamHelper.getParticipantDto(contestDto.getParticipantTwoId()));
        }
        return contestDto;
    }

    @Override
    public Iterable<ContestDTO> getContestDTOsWithParticipants(Integer tournamentId) {
        Map<Integer, PlayerDTO> players = teamHelper.getTournamentPlayerDtoMap(tournamentId);
        Iterable<ContestDTO> contests = super.getContestDTOs(tournamentId);

        if (contests == null) return null;
        StreamSupport.stream(contests.spliterator(), true)
                .forEach(contest -> {
                    if (contest instanceof DoubleContestDTO) {
                        TeamDTO teamOne = ((DoubleContestDTO) contest).getTeamOne();
                        TeamDTO teamTwo = ((DoubleContestDTO) contest).getTeamTwo();

                        teamOne.setPlayerOne(players.get(teamOne.getPlayerOne().getId()));
                        teamOne.setPlayerTwo(players.get(teamOne.getPlayerTwo().getId()));
                        teamTwo.setPlayerOne(players.get(teamTwo.getPlayerOne().getId()));
                        teamTwo.setPlayerTwo(players.get(teamTwo.getPlayerTwo().getId()));
                    }
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
