package ua.com.dss.tennis.tournament.api.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.dss.tennis.tournament.api.exception.handler.WarningHandler;
import ua.com.dss.tennis.tournament.api.model.definitions.Data;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;
import ua.com.dss.tennis.tournament.api.model.definitions.Meta.PageableMeta;
import ua.com.dss.tennis.tournament.api.model.definitions.Pageable.PageableTypedResponse;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PageableDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ResponseWarningDTO;

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

    public <D extends PageableTypedResponse<? extends Data>> D convertPageable(ResponseWarningDTO<PageableDTO> source,
                                                                               Class<D> destinationClass) {
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
