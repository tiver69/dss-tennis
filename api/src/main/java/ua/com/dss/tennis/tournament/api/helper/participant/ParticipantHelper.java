package ua.com.dss.tennis.tournament.api.helper.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PageableDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ResourceObjectDTO;
import ua.com.dss.tennis.tournament.api.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public abstract class ParticipantHelper<T, D> {

    @Autowired
    protected PlayerRepository playerRepository;
    @Autowired
    protected ConverterHelper converterHelper;

    public abstract Integer saveParticipant(D participantDto);

    public abstract boolean isParticipantExist(Integer playerId);

    public abstract boolean isParticipantNotExist(Integer participantId);

    public abstract T getParticipant(Integer participantId);

    public abstract PageableDTO<D> getParticipantPage(Pageable pageableRequestParameter);

    public abstract D getParticipantDto(Integer participantId);

    public abstract List<T> getTournamentParticipants(Integer tournamentId);

    public abstract List<PlayerDTO> getTournamentPlayerDTOs(Integer tournamentId);

    public abstract ArrayList<Integer> getTournamentPlayerIds(Integer tournamentId);

    public abstract List<Integer> getParticipantIdsForEnrolling(Integer tournamentId,
                                                                List<ResourceObjectDTO> newParticipants,
                                                                Set<ErrorDataDTO> warnings);

    public Map<Integer, PlayerDTO> getTournamentPlayerDtoMap(Integer tournamentId) {
        List<PlayerDTO> players = getTournamentPlayerDTOs(tournamentId);
        return players.stream().collect(Collectors.toMap(PlayerDTO::getId, playerDTO -> playerDTO));
    }
}
