package com.dss.tennis.tournament.tables.validator.participant;

import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class ParticipantValidator<T> {

    @Autowired
    protected WarningHandler warningHandler;

    public abstract ErrorData validateParticipantForEnrolling(List<Integer> currentPlayerIds,
                                                              ResourceObjectDTO newParticipant);
}
