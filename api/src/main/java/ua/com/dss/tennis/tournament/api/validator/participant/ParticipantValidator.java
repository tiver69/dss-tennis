package ua.com.dss.tennis.tournament.api.validator.participant;

import org.springframework.beans.factory.annotation.Autowired;
import ua.com.dss.tennis.tournament.api.exception.handler.WarningHandler;
import ua.com.dss.tennis.tournament.api.helper.factory.TournamentFactory;
import ua.com.dss.tennis.tournament.api.helper.participant.PlayerHelper;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ResourceObjectDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;

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
