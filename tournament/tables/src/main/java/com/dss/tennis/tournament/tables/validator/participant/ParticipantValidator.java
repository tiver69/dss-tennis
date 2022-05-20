package com.dss.tennis.tournament.tables.validator.participant;

import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.helper.factory.TournamentFactory;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class ParticipantValidator<T> {

    @Autowired
    protected WarningHandler warningHandler;
    @Autowired
    protected PlayerHelper playerHelper;
    @Autowired
    protected TournamentFactory tournamentFactory;

    public abstract void validateTournamentParticipantQuantity(TournamentDTO tournamentDto,
                                                               int additionalParticipantQuantity);

    public abstract ErrorDataDTO validateParticipantForEnrolling(List<Integer> currentPlayerIds,
                                                                 ResourceObjectDTO newParticipant);

    public abstract void validateParticipantForRemoving(Integer participantId, Integer tournamentId);
}
