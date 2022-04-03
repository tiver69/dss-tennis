package com.dss.tennis.tournament.tables.helper.participant;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public abstract D getParticipantDto(Integer participantId);

    public abstract List<T> getTournamentParticipants(Integer tournamentId);

    public abstract List<PlayerDTO> getTournamentPlayerDTOs(Integer tournamentId);

    public abstract ArrayList<Integer> getTournamentPlayerIds(Integer tournamentId);

    public abstract Set<Integer> getParticipantIdsForEnrolling(Integer tournamentId,
                                                               List<ResourceObjectDTO> newParticipants,
                                                               Set<ErrorDataDTO> warnings);
}
