package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.factory.TournamentFactory;
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
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.TOURNAMENT_NOT_FOUND;

@Service
public class TournamentHelper {

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private TournamentFactory tournamentFactory;

    @Transactional
    public Tournament saveTournament(TournamentDTO tournamentDTO) {
        Tournament tournament = Tournament.builder()
                .id(tournamentDTO.getId())
                .name(tournamentDTO.getName())
                .tournamentType(tournamentDTO.getTournamentType())
                .participantType(tournamentDTO.getParticipantType())
                .status(getStatusBaseOnBeginningDate(tournamentDTO.getBeginningDate()))
                .beginningDate(tournamentDTO.getBeginningDate())
                .build();
        return tournamentRepository.save(tournament);
    }

    public void addParticipantsToTournament(TournamentDTO tournamentDto, List<Integer> newParticipantIds) {
        tournamentFactory.createContestForNewParticipants(tournamentDto, newParticipantIds);
    }

    @Transactional
    public void removeParticipantFromTournament(Integer participantId, TournamentDTO tournamentDto,
                                                boolean techDefeat) {
        tournamentFactory.removeParticipantFromTournament(participantId, tournamentDto, techDefeat);
    }

    @Transactional
    public void removeTournament(TournamentDTO tournamentDto) {
        tournamentFactory.removeTournamentContests(tournamentDto);
        tournamentRepository.deleteById(tournamentDto.getId());
    }

    public TournamentDTO getTournamentDto(Integer tournamentId) {
        return getTournamentDto(tournamentId, RequestParameter.DEFAULT);
    }

    public PageableDTO<TournamentDTO> getTournamentsPage(Pageable pageableRequestParameter) {
        Page<Tournament> tournamentsPage = tournamentRepository.findAll(pageableRequestParameter);
        List<TournamentDTO> tournaments = tournamentsPage.getContent().stream()
                .map(tournament -> tournamentFactory.populateTournamentDTO(tournament))
                .collect(Collectors.toList());

        return PageableDTO.<TournamentDTO>builder().page(tournaments)
                .currentPage(pageableRequestParameter.getPageNumber())
                .pageSize(pageableRequestParameter.getPageSize())
                .totalPages(tournamentsPage.getTotalPages())
                .build();
    }

    public TournamentDTO getTournamentDto(Integer tournamentId, RequestParameter requestParameters) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->
                new DetailedException(TOURNAMENT_NOT_FOUND, tournamentId.toString()));

        return tournamentFactory.populateTournamentDTO(tournament, requestParameters);
    }

    private StatusType getStatusBaseOnBeginningDate(LocalDate beginningDate) {
        if (beginningDate == null || beginningDate.isAfter(LocalDate.now()))
            return StatusType.PLANNED;
        return StatusType.IN_PROGRESS;
    }
}
