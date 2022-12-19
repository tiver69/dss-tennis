package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.converter.modelmapper.ModelMapperFactory;
import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.model.definitions.Meta.PageableMeta;
import com.dss.tennis.tournament.tables.model.definitions.PageableResponse;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.ResponseWarningDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorResponse.ErrorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConverterHelper {

    @Autowired
    private WarningHandler warningHandler;

    public <S, D> D convert(S source, Class<D> destinationClass) {
        Object destinationObject = ModelMapperFactory.getStatelessInstance()
                .map(source, destinationClass);

        return destinationClass.cast(destinationObject);
    }

    public <S, D> D convert(S source, Class<D> destinationClass, Integer extraInteger) {
        Object destinationObject = ModelMapperFactory.getStatefulInstance(extraInteger)
                .map(source, destinationClass);

        return destinationClass.cast(destinationObject);
    }

    public <D extends PageableResponse> D convert(ResponseWarningDTO<?> source, Class<D> destinationClass) {
        D destination = convert(source.getData(), destinationClass);

        Set<ErrorDataDTO> sourceWarnings = source.getWarnings();
        if (sourceWarnings != null && !sourceWarnings.isEmpty()) {
            Set<ErrorData> destinationWarnings = sourceWarnings.stream().map(warningHandler::createErrorData)
                    .collect(Collectors.toSet());
            PageableMeta destinationMeta = destination.getMeta() == null ? new PageableMeta() : destination.getMeta();
            destinationMeta.setWarnings(destinationWarnings);
            destination.setMeta(destinationMeta);
        }
        return destination;
    }
}
