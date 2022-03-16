package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.factory.TournamentFactory;
import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.StatusType;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.RequestParameter;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NOT_FOUND;

@Service
public class TournamentHelper {

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private TournamentFactory tournamentFactory;

    @Transactional
    public Tournament createNewTournamentWithContests(TournamentDTO tournamentDTO) {
        Tournament tournament = createNewTournament(tournamentDTO);

        tournamentFactory
                .createContestsForTournament(tournament, tournamentDTO.getPlayers(), tournamentDTO.getTournamentType());
        return tournament;
    }

    public void addPlayersToTournament(TournamentDTO tournamentDto, List<Integer> currentPlayerIds,
                                       Set<Integer> newPlayerIds) {
        tournamentFactory
                .createContestForNewPlayers(tournamentDto, currentPlayerIds, newPlayerIds);
    }

    public Tournament createNewTournament(TournamentDTO tournamentDTO) {
        Tournament tournament = Tournament.builder().name(tournamentDTO.getName())
                .tournamentType(tournamentDTO.getTournamentType())
                .participantType(ParticipantType.SINGLE)
                .status(getStatusBaseOnBeginningDate(tournamentDTO.getBeginningDate()))
                .beginningDate(tournamentDTO.getBeginningDate())
                .build();
        return tournamentRepository.save(tournament);
    }

    public TournamentDTO getTournament(Integer tournamentId) {
        return getTournament(tournamentId, RequestParameter.DEFAULT);
    }

    public PageableDTO<TournamentDTO> getTournamentsPage(Pageable pageableRequestParameter) {
        Page<Tournament> tournamentsPage = tournamentRepository.findAll(pageableRequestParameter);
        List<TournamentDTO> tournaments = tournamentsPage.getContent().stream()
                .map(tournament -> tournamentFactory.populateTournamentDTO(tournament))
                .collect(Collectors.toList());

        return PageableDTO.<TournamentDTO>builder().page(tournaments)
                .currentPage(pageableRequestParameter.getPageNumber()).totalPages(tournamentsPage.getTotalPages())
                .build();
    }

    public TournamentDTO getTournament(Integer tournamentId, RequestParameter requestParameters) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->
                new DetailedException(TOURNAMENT_NOT_FOUND, tournamentId.toString()));

        return tournamentFactory.populateTournamentDTO(tournament, requestParameters);
    }

    public StatusType getStatusBaseOnBeginningDate(LocalDate beginningDate) {
        if (beginningDate == null || beginningDate.isAfter(LocalDate.now()))
            return StatusType.PLANNED;
        return StatusType.IN_PROGRESS;
    }
}
