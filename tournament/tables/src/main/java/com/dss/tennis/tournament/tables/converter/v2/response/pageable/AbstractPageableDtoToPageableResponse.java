package com.dss.tennis.tournament.tables.converter.v2.response.pageable;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.Meta.PageableMeta;
import com.dss.tennis.tournament.tables.model.definitions.PageableResponse;
import com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.modelmapper.ModelMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class AbstractPageableDtoToPageableResponse<S, D extends PageableResponse> {

    @Getter
    protected final ModelMapper modelMapper;

    public D convertPageableResponse(PageableDTO<S> source, Class<D> destinationClass) {
        try {
            D response = destinationClass.getConstructor().newInstance();
            List responseData = source.getPage().stream()
                    .map(pageItem -> modelMapper.map(pageItem, response.getResponseDataClass())) //todo cast
                    .collect(Collectors.toList());

            response.setMeta(PageableMeta.builder().totalPages(source.getTotalPages())
                    .currentPage(source.getCurrentPage() + 1).build());
            response.setData(responseData);
            response.setLinks(convertPageableLinks(source.getTotalPages(), source
                    .getPageSize(), source.getCurrentPage() + 1, response.getResponseDataType()));

            return response;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new DetailedException(ErrorConstants.ErrorKey.INTERNAL_SERVER_ERROR, "Failed to create response object");
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
