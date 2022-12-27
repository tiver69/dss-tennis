package ua.com.dss.tennis.tournament.api.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.helper.factory.TournamentFactory;
import ua.com.dss.tennis.tournament.api.model.db.v1.Tournament;
import ua.com.dss.tennis.tournament.api.model.dto.PageableDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;
import ua.com.dss.tennis.tournament.api.repository.TournamentRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.TOURNAMENT_NOT_FOUND;

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

    public PageableDTO<TournamentDTO> getTournamentsPage(Pageable pageableRequestParameter) {
        Page<Tournament> tournamentsPage = tournamentRepository.findAll(pageableRequestParameter);
        List<TournamentDTO> tournaments = tournamentsPage.getContent().stream()
                .map(tournament -> tournamentFactory.populateTournamentDTO(tournament))
                .collect(Collectors.toList());

        return PageableDTO.<TournamentDTO>builder().page(tournaments)
                .currentPage(pageableRequestParameter.getPageNumber())
                .totalPages(tournamentsPage.getTotalPages())
                .pageSize(pageableRequestParameter.getPageSize())
                .totalPages(tournamentsPage.getTotalPages())
                .build();
    }

    public TournamentDTO getTournamentDto(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->
                new DetailedException(TOURNAMENT_NOT_FOUND, tournamentId.toString()));

        return tournamentFactory.populateTournamentDTO(tournament);
    }

    public TournamentDTO getTournamentDTOWithContests(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->
                new DetailedException(TOURNAMENT_NOT_FOUND, tournamentId.toString()));

        return tournamentFactory.populateTournamentDtoWithContests(tournament);
    }
}
