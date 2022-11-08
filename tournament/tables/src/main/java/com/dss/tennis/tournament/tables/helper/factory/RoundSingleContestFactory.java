package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoundSingleContestFactory extends RoundContestFactory {

    @Autowired
    private PlayerHelper playerHelper;

    @Override
    public void createContestsForTournament(Integer tournamentId, List<Integer> newPlayerIds) {
        List<Integer> currentPlayerIds = playerHelper.getTournamentParticipants(tournamentId).stream()
                .map(Player::getId).collect(Collectors.toList());
        for (Integer newPlayerId : newPlayerIds) {
            for (Integer currentPlayerId : currentPlayerIds)
                contestHelper.createNewSingleContest(newPlayerId, currentPlayerId, tournamentId);
            currentPlayerIds.add(newPlayerId);
        }
    }

    @Override
    public void removeParticipantFromTournament(Integer playerId, int tournamentId, boolean techDefeat) {
        if (techDefeat)
            contestHelper.getTournamentPlayerContests(playerId, tournamentId).forEach(contestDTO -> contestHelper
                    .updateSingleContestTechDefeatForPlayerRemoving(playerId, contestDTO));
        else
            removeContests(() -> contestRepository.findByPlayerIdAndSingleTournamentId(playerId, tournamentId));
    }

    @Override
    public ContestDTO getContestDTO(Integer contestId, Integer tournamentId) {
        SingleContestDTO contestDto = (SingleContestDTO) getBasicContestDTO(contestId, tournamentId);
        PlayerDTO playerOne = playerHelper.getParticipantDto(contestDto.participantOneId());
        PlayerDTO playerTwo = playerHelper.getParticipantDto(contestDto.participantTwoId());

        contestDto.setPlayerOne(playerOne);
        contestDto.setPlayerTwo(playerTwo);
        return contestDto;
    }

    @Override
    public Iterable<ContestDTO> getContestDTOs(Integer tournamentId, Map<Integer, PlayerDTO> players) {
        Iterable<ContestDTO> contests = super.getContestDTOs(tournamentId);
        contests.forEach(contest -> {
            ((SingleContestDTO) contest)
                    .setPlayerOne(players.get(contest.participantOneId()));
            ((SingleContestDTO) contest)
                    .setPlayerTwo(players.get(contest.participantTwoId()));
        });
        return contests;
    }

    @Override
    public Class<? extends ContestDTO> getContestParticipantDtoClass() {
        return SingleContestDTO.class;
    }

    @Override
    public Class<? extends Contest> getContestParticipantClass() {
        return SingleContest.class;
    }
}
