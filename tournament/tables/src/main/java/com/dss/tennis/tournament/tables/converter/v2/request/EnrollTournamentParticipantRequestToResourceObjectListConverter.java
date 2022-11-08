package com.dss.tennis.tournament.tables.converter.v2.request;

import com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.tournament.EnrollTournamentParticipantRequest;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.List;

public class EnrollTournamentParticipantRequestToResourceObjectListConverter implements Converter<EnrollTournamentParticipantRequest, List> {

    @Override
    public List<ResourceObjectDTO> convert(MappingContext<EnrollTournamentParticipantRequest, List> context) {
        EnrollTournamentParticipantRequest participantsResourceObject = context.getSource();
        List<SimpleResourceObject> participantsToEnroll = participantsResourceObject.getRelationships()
                .getParticipants();

        List<ResourceObjectDTO> resourceObjectDtoList = new ArrayList<>();
        for (byte i = 0; i < participantsToEnroll.size(); i++) {
            ResourceObjectDTO participantResourceObject = new ResourceObjectDTO(participantsToEnroll.get(i).getId(),
                    ResourceObjectType.fromStringValue(participantsToEnroll.get(i).getType()));
            participantResourceObject.setSequenceNumber(i);
            resourceObjectDtoList.add(participantResourceObject);
        }
        return resourceObjectDtoList;
    }
}