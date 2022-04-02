package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.model.db.v1.Team;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.DoubleContest;
import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import com.dss.tennis.tournament.tables.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.CONTEST_NOT_FOUND;

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

    public ContestDTO getTournamentContest(Integer contestId, Integer tournamentId) {
        return getBaseContestDTO(() -> contestRepository.findByIdAndTournamentId(contestId, tournamentId));
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

    public ContestDTO getContestWithScore(Integer contestId) {
        ContestDTO contestDto = getBaseContestDTO(() -> contestRepository.findById(contestId));
        ScoreDTO scoreDto = scoreHelper.getContestScoreDto(contestId);

        contestDto.setScoreDto(scoreDto);
        return contestDto;
    }

    private ContestDTO getBaseContestDTO(Supplier<Optional<Contest>> contestSupplier) {
        Contest contest = contestSupplier.get().orElseThrow(() -> new DetailedException(CONTEST_NOT_FOUND));
        Class<? extends ContestDTO> destinationClass = contest instanceof SingleContest ? SingleContestDTO.class :
                DoubleContestDTO.class;
        return converterHelper.convert(contest, destinationClass);
    }

    public Contest createNewSingleContest(Integer playerOneId, Integer playerTwoId, Integer tournamentId) {
        Contest contest = SingleContest.builder()
                .playerOneId(playerOneId)
                .playerTwoId(playerTwoId)
                .tournamentId(tournamentId)
                .build();

        return contestRepository.save(contest);
    }

    public Contest createNewDoubleContest(Team teamOne, Team teamTwo, Integer tournamentId) {
        Contest contest = DoubleContest.builder()
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .tournamentId(tournamentId)
                .build();

        return contestRepository.save(contest);
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

    public void updateSingleContestTechDefeatForPlayerRemoving(Integer playerId, SingleContestDTO contestDto) {
        if (!contestDto.isTechDefeat()) {
            Integer winnerId = contestDto.getPlayerOne().getId() == playerId ? contestDto.getPlayerTwo()
                    .getId() : contestDto.getPlayerOne().getId();
            contestRepository.updateTechDefeatByContestId(winnerId, true, contestDto.getId());
        } else if (contestDto.getWinner().equals(playerId)) {
            contestRepository.updateTechDefeatByContestId(null, true, contestDto.getId());
        }
    }

    public void updateDoubleContestTechDefeatForTeamRemoving(Integer teamId, DoubleContestDTO contestDto) {
        if (!contestDto.isTechDefeat()) {
            Integer winnerId = contestDto.getTeamOne().getId().equals(teamId) ? contestDto.getTeamTwo()
                    .getId() : contestDto.getTeamOne().getId();
            contestRepository.updateTechDefeatByContestId(winnerId, true, contestDto.getId());
        } else if (contestDto.getWinner().equals(teamId)) {
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
}
