package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.factory.TournamentFactory;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.DoubleContest;
import com.dss.tennis.tournament.tables.model.db.v2.EliminationContest;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContestHelper {

    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private ScoreHelper scoreHelper;
    @Autowired
    private TournamentFactory tournamentFactory;

    public ContestDTO getTournamentContestDTO(Integer contestId, TournamentDTO tournament) {
        return tournamentFactory.getTournamentContestDTO(contestId, tournament);
    }

    public List<Contest> getTournamentContests(Integer tournamentId) {
        return contestRepository.findByTournamentId(tournamentId);
    }

    public List<SingleContestDTO> getTournamentPlayerContests(Integer playerId, Integer tournamentId) {
        List<Contest> contests = contestRepository.findByPlayerIdAndSingleTournamentId(playerId, tournamentId);
        return contests.stream().map(contest -> converterHelper.convert(contest, SingleContestDTO.class))
                .collect(Collectors.toList());
    }

    public List<DoubleContestDTO> getTournamentTeamContests(Integer teamId, Integer tournamentId) {
        List<Contest> contests = contestRepository.findByTeamIdAndDoubleTournamentId(teamId, tournamentId);
        return contests.stream().map(contest -> converterHelper.convert(contest, DoubleContestDTO.class))
                .collect(Collectors.toList());
    }

    public Contest createNewSingleContest(Integer playerOneId, Integer playerTwoId, Integer tournamentId) {
        Contest contest = SingleContest.builder()
                .playerOneId(playerOneId)
                .playerTwoId(playerTwoId)
                .tournamentId(tournamentId)
                .participantOneScore(scoreHelper.mapEmptyParticipantScore())
                .participantTwoScore(scoreHelper.mapEmptyParticipantScore())
                .build();

        return contestRepository.save(contest);
    }

    public Contest createNewDoubleContest(Team teamOne, Team teamTwo, Integer tournamentId) {
        Contest contest = DoubleContest.builder()
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .tournamentId(tournamentId)
                .participantOneScore(scoreHelper.mapEmptyParticipantScore())
                .participantTwoScore(scoreHelper.mapEmptyParticipantScore())
                .build();

        return contestRepository.save(contest);
    }

    public Contest createNewEliminationContest(Integer firstParentContestId, Integer secondParentContestId,
                                               Integer tournamentId) {
        Contest contest = EliminationContest.builder()
                .firstParentContestId(firstParentContestId)
                .secondParentContestId(secondParentContestId)
                .tournamentId(tournamentId)
                .participantOneScore(scoreHelper.mapEmptyParticipantScore())
                .participantTwoScore(scoreHelper.mapEmptyParticipantScore())
                .build();

        return contestRepository.save(contest);
    }

    public void updateContestScore(ScoreDTO scoreDto, ContestDTO contestDto) {
        scoreHelper.updateContestScore(scoreDto);

        Integer winnerId = scoreHelper.getWinnerIdFunctionByUpdatedScore(scoreDto).apply(contestDto);
        contestRepository.updateWinnerIdByContestId(winnerId, contestDto.getId());
    }

    public void updateContestTechDefeatForParticipantRemoving(Integer participantId, ContestDTO contestDto) {
        ScoreDTO scoreDto = contestDto.getScoreDto();
        int techDefeatScoreId;
        if (participantId.equals(contestDto.getParticipantOneId())) {
            techDefeatScoreId = scoreDto.getParticipantOneScoreId();
            scoreDto.getTechDefeat().setParticipantOne(true);
        } else {
            techDefeatScoreId = scoreDto.getParticipantTwoScoreId();
            scoreDto.getTechDefeat().setParticipantTwo(true);
        }

        Integer winnerId = scoreHelper.getTechDefeatWinnerIdFunction(scoreDto.getTechDefeat()).apply(contestDto);
        scoreHelper.updateContestScoreTechDefeat(techDefeatScoreId);
        contestRepository.updateWinnerIdByContestId(winnerId, contestDto.getId());
    }

    public void removeContest(Contest contest) {
        contestRepository.delete(contest);
    }

    public void removeContestById(Integer contestId) {
        contestRepository.deleteById(contestId);
    }

    public boolean isEliminationContestChildScoreDefined(Integer contestId) {
        return !contestRepository.isEliminationContestFinal(contestId) && (
                contestRepository.isEliminationContestChildScoreDefined(contestId) || contestRepository
                        .isEliminationContestChildTechDefeat(contestId));
    }
}
