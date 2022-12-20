package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.StreamSupport;

@Service
public class EliminationSingleContestFactory extends EliminationContestFactory {

    @Autowired
    private PlayerHelper playerHelper;

    @Override
    public Integer createFirstLineEliminationContest(Integer firstParticipantId, Integer secondParticipantId,
                                                     Integer tournamentId) {
        return contestHelper.createNewSingleContest(firstParticipantId, secondParticipantId, tournamentId).getId();
    }

    @Override
    protected ContestDTO convertEliminationContestToBase(EliminationContestDTO eliminationContest) {
        PlayerDTO firstParticipant = getEliminationContestParticipantFromParent(eliminationContest
                .getFirstParentContestDto());
        PlayerDTO secondParticipant = getEliminationContestParticipantFromParent(eliminationContest
                .getSecondParentContestDto());
        SingleContestDTO contestDTO = converterHelper.convert(eliminationContest, SingleContestDTO.class);
        contestDTO.setPlayerOne(firstParticipant);
        contestDTO.setPlayerTwo(secondParticipant);
        return contestDTO;
    }

    private PlayerDTO getEliminationContestParticipantFromParent(ContestDTO parentContestDTO) {
        Integer winnerId = parentContestDTO.getWinnerId();
        if (winnerId == null) return null;
        if (parentContestDTO instanceof SingleContestDTO) {
            SingleContestDTO contestDTO = (SingleContestDTO) parentContestDTO;
            if (contestDTO.getPlayerOne().getId() == winnerId)
                return contestDTO.getPlayerOne();
            if (contestDTO.getPlayerTwo().getId() == winnerId)
                return contestDTO.getPlayerTwo();
        }
        EliminationContestDTO parentEliminationContest = (EliminationContestDTO) parentContestDTO;
        if (winnerId.equals(parentEliminationContest.getFirstParentContestDto().getWinnerId()))
            return getEliminationContestParticipantFromParent(parentEliminationContest.getFirstParentContestDto());
        if (winnerId.equals(parentEliminationContest.getSecondParentContestDto().getWinnerId()))
            return getEliminationContestParticipantFromParent(parentEliminationContest.getSecondParentContestDto());
        return null;
    }

    @Override
    public Class<? extends ContestDTO> getContestParticipantDtoClass() {
        return SingleContestDTO.class;
    }

    @Override
    public ContestDTO getContestDTO(Integer contestId, Integer tournamentId) {
        ContestDTO contestDto = getBasicContestDTO(contestId, tournamentId);
        if (contestDto instanceof EliminationContestDTO) {
            ((EliminationContestDTO) contestDto).forEach(parentContestDTO -> {
                if (parentContestDTO instanceof SingleContestDTO) {
                    Integer playerOneId = ((SingleContestDTO) parentContestDTO).getPlayerOne().getId();
                    Integer playerTwoId = ((SingleContestDTO) parentContestDTO).getPlayerTwo().getId();
                    ((SingleContestDTO) parentContestDTO).setPlayerOne(playerHelper.getParticipantDto(playerOneId));
                    ((SingleContestDTO) parentContestDTO).setPlayerTwo(playerHelper.getParticipantDto(playerTwoId));
                }
            });
        } else {
            PlayerDTO playerOne = playerHelper.getParticipantDto(contestDto.getParticipantOneId());
            PlayerDTO playerTwo = playerHelper.getParticipantDto(contestDto.getParticipantTwoId());
            ((SingleContestDTO) contestDto).setPlayerOne(playerOne);
            ((SingleContestDTO) contestDto).setPlayerTwo(playerTwo);
        }
        return contestDto;
    }

    @Override
    public Iterable<ContestDTO> getContestDTOsWithParticipants(Integer tournamentId) {
        Map<Integer, PlayerDTO> players = playerHelper.getTournamentPlayerDtoMap(tournamentId);
        Iterable<ContestDTO> contests = super.getContestDTOs(tournamentId);

        if (contests == null) return null;
        StreamSupport.stream(contests.spliterator(), true)
                .forEach(contest -> {
                    if (contest instanceof SingleContestDTO) {
                        SingleContestDTO singleContest = (SingleContestDTO) contest;
                        singleContest.setPlayerOne(players.get(contest.getParticipantOneId()));
                        singleContest.setPlayerTwo(players.get(contest.getParticipantTwoId()));
                    }
                });
        return contests;
    }

    @Override
    public Class<? extends Contest> getContestParticipantClass() {
        return SingleContest.class;
    }
}