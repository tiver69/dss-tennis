package ua.com.dss.tennis.tournament.api.helper.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.dss.tennis.tournament.api.helper.participant.PlayerHelper;
import ua.com.dss.tennis.tournament.api.model.db.v1.Player;
import ua.com.dss.tennis.tournament.api.model.db.v2.Contest;
import ua.com.dss.tennis.tournament.api.model.db.v2.SingleContest;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.SingleContestDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
                    .updateContestTechDefeatForParticipantRemoving(playerId, contestDTO));
        else
            removeContests(() -> contestRepository.findByPlayerIdAndSingleTournamentId(playerId, tournamentId));
    }

    @Override
    public ContestDTO getContestDTO(Integer contestId, Integer tournamentId) {
        SingleContestDTO contestDto = (SingleContestDTO) getBasicContestDTO(contestId, tournamentId);
        PlayerDTO playerOne = playerHelper.getParticipantDto(contestDto.getParticipantOneId());
        PlayerDTO playerTwo = playerHelper.getParticipantDto(contestDto.getParticipantTwoId());

        contestDto.setPlayerOne(playerOne);
        contestDto.setPlayerTwo(playerTwo);
        return contestDto;
    }

    @Override
    public Iterable<ContestDTO> getContestDTOsWithParticipants(Integer tournamentId) {
        Map<Integer, PlayerDTO> players = playerHelper.getTournamentPlayerDtoMap(tournamentId);
        Iterable<ContestDTO> contests = super.getContestDTOs(tournamentId);

        return StreamSupport.stream(contests.spliterator(), true).map(SingleContestDTO.class::cast).peek(contest -> {
            contest.setPlayerOne(players.get(contest.getParticipantOneId()));
            contest.setPlayerTwo(players.get(contest.getParticipantTwoId()));
        }).collect(Collectors.toList());
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
