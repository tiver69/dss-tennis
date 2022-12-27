package ua.com.dss.tennis.tournament.api.converter.response.pageable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants;
import ua.com.dss.tennis.tournament.api.model.definitions.Data;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.Meta.PageableMeta;
import ua.com.dss.tennis.tournament.api.model.definitions.Pageable.PageableTypedResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType;
import ua.com.dss.tennis.tournament.api.model.dto.PageableDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class AbstractPageableDtoToPageableResponse<S, D extends PageableTypedResponse<? extends Data>> {

    @Getter
    protected final ModelMapper modelMapper;

    public D convertPageableResponse(PageableDTO<S> source, Class<D> destinationClass) {
        try {
            D response = destinationClass.getConstructor().newInstance();
            List responseData = source.getPage().stream()
                    .map(pageItem -> modelMapper.map(pageItem, response.getResponseDataClass()))
                    .collect(Collectors.toList());

            response.setMeta(PageableMeta.builder().totalPages(source.getTotalPages())
                    .currentPage(source.getCurrentPage() + 1).build());
            response.setData(responseData);
            response.setLinks(convertPageableLinks(source.getTotalPages(), source.getPageSize(), source
                    .getCurrentPage() + 1, response.getResponseDataType()));

            return response;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new DetailedException(ErrorConstants.ErrorKey.INTERNAL_SERVER_ERROR, "Failed to create response " +
                    "object");
        }
    }

    private Links convertPageableLinks(int totalPages, int pageSize, int currentPage, ResourceObjectType type) {
        return Links.builder()
                .first(String.format(type.pageableLinkFormat, 1, pageSize))
                .last(String.format(type.pageableLinkFormat, totalPages, pageSize))
                .prev(currentPage != 1 ?
                        String.format(type.pageableLinkFormat, currentPage - 1, pageSize) : null)
                .self(String.format(type.pageableLinkFormat, currentPage, pageSize))
                .next(currentPage != totalPages ?
                        String.format(type.pageableLinkFormat, currentPage + 1, pageSize) : null)
                .build();
    }
}
