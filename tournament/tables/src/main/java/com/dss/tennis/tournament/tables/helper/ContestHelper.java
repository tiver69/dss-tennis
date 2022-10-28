package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.factory.TournamentFactory;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.db.v2.*;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import com.dss.tennis.tournament.tables.repository.TeamRepository;
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
    private TeamRepository teamRepository;
    @Autowired
    private ScoreHelper scoreHelper;
    @Autowired
    private TournamentFactory tournamentFactory;

    public ContestDTO getTournamentContestDTO(Integer contestId, TournamentDTO tournament, boolean includeScore) {
        ContestDTO contestDto = tournamentFactory.getTournamentContestDTO(contestId, tournament);
        if (includeScore) {
            ScoreDTO scoreDto = scoreHelper.getContestScoreDto(contestId);
            contestDto.setScoreDto(scoreDto);
        }
        return contestDto;
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

    public Contest createNewSingleContest(Integer playerOneId, Integer playerTwoId, Integer tournamentId,
                                          boolean shouldCreateScore) {
        Contest contest = SingleContest.builder()
                .playerOneId(playerOneId)
                .playerTwoId(playerTwoId)
                .tournamentId(tournamentId)
                .build();

        Contest createdContest = contestRepository.save(contest);
        if (shouldCreateScore)
            scoreHelper.createEmptyContestScore(createdContest.getId());
        return createdContest;
    }

    public Contest createNewDoubleContest(Team teamOne, Team teamTwo, Integer tournamentId, boolean shouldCreateScore) {
        Contest contest = DoubleContest.builder()
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .tournamentId(tournamentId)
                .build();

        Contest createdContest = contestRepository.save(contest);
        if (shouldCreateScore)
            scoreHelper.createEmptyContestScore(createdContest.getId());
        return createdContest;
    }

    public Contest createNewEliminationContest(Integer firstParentContestId, Integer secondParentContestId,
                                               Integer tournamentId, boolean shouldCreateScore) {
        Contest contest = EliminationContest.builder()
                .firstParentContestId(firstParentContestId)
                .secondParentContestId(secondParentContestId)
                .tournamentId(tournamentId)
                .build();

        Contest createdContest = contestRepository.save(contest);
        if (shouldCreateScore)
            scoreHelper.createEmptyContestScore(createdContest.getId());
        return createdContest;
    }

    public void createContestScore(ScoreDTO scoreDto, ContestDTO contestDto) {
        scoreHelper.createContestScore(scoreDto, contestDto.getId());
        if (!contestDto.isTechDefeat()) { //todo
            Integer winnerId = scoreHelper.getScoreWinnerIdFunction(scoreDto.getSets()).apply(contestDto);
            contestRepository.updateWinnerIdByContestId(winnerId, contestDto.getId());
        }
    }

    public void updateContestScore(ScorePatchDTO scoreDto, ContestDTO contestDto) {
        scoreHelper.updateContestScore(scoreDto, contestDto.getId());
        if (!contestDto.isTechDefeat()) {
            Integer winnerId = scoreHelper.getScoreWinnerIdFunction(scoreDto.getSets()).apply(contestDto);
            contestRepository.updateWinnerIdByContestId(winnerId, contestDto.getId());
        }
    }

    public void updateContestScore(ScoreDTO scoreDto, ContestDTO contestDto) {
        scoreHelper.updateContestScore(scoreDto);
        if (scoreDto.getTechDefeat().isTechDefeat()) {
            updateContestTechDefeat(scoreDto.getTechDefeat(), contestDto);
        } else {
            Integer winnerId = scoreHelper.getScoreWinnerIdFunctionWithCreatedScore(scoreDto.getSets()).apply(contestDto);
            contestRepository.updateWinnerIdByContestId(winnerId, contestDto.getId());
        }
    }

    public void updateSingleContestTechDefeatForPlayerRemoving(Integer playerId, SingleContestDTO contestDto) {
        if (!contestDto.isTechDefeat()) {
            Integer winnerId = contestDto.getPlayerOne().getId() == playerId ? contestDto.getPlayerTwo()
                    .getId() : contestDto.getPlayerOne().getId();
            contestRepository.updateTechDefeatByContestId(winnerId, true, contestDto.getId());
        } else if (contestDto.getWinnerId().equals(playerId)) {
            contestRepository.updateTechDefeatByContestId(null, true, contestDto.getId());
        }
    }

    public void updateDoubleContestTechDefeatForTeamRemoving(Integer teamId, DoubleContestDTO contestDto) {
        if (!contestDto.isTechDefeat()) {
            Integer winnerId = contestDto.getTeamOne().getId().equals(teamId) ? contestDto.getTeamTwo()
                    .getId() : contestDto.getTeamOne().getId();
            contestRepository.updateTechDefeatByContestId(winnerId, true, contestDto.getId());
        } else if (contestDto.getWinnerId().equals(teamId)) {
            contestRepository.updateTechDefeatByContestId(null, true, contestDto.getId());
        }
    }

    public void updateContestTechDefeat(TechDefeatDTO techDefeatDto, ContestDTO contestDto) {
        if (techDefeatDto.isTechDefeat()) {
            Integer winnerId = scoreHelper.getTechDefeatWinnerIdFunction(techDefeatDto).apply(contestDto);
            contestRepository.updateTechDefeatByContestId(winnerId, true, contestDto.getId());
        }
        if (contestDto.isTechDefeat() && !techDefeatDto.isTechDefeat()) {
            List<SetScore> sets = scoreHelper.getContestSetScores(contestDto.getId());
            ScoreDTO scoreDTO = scoreHelper.mapSetScoreToDto(sets);

            Integer winnerId = scoreHelper.getScoreWinnerIdFunction(scoreDTO.getSets()).apply(contestDto);
            contestRepository.updateTechDefeatByContestId(winnerId, false, contestDto.getId());
        }
    }

    public void removeContestById(Integer contestId) {
        scoreHelper.removeContestScore(contestId);
        contestRepository.deleteById(contestId);
    }

    public boolean isEliminationContestChildTechDefeat(Integer contestId) {
        return contestRepository.isEliminationContestChildTechDefeat(contestId);
    }
}
