package ua.com.dss.tennis.tournament.api.converter.request;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.EnrollTournamentParticipantRequest;
import ua.com.dss.tennis.tournament.api.model.dto.ResourceObjectDTO;

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