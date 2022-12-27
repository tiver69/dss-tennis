package ua.com.dss.tennis.tournament.api.model.definitions;

import ua.com.dss.tennis.tournament.api.model.definitions.Meta.PageableMeta;

import java.util.List;

public interface Pageable {

    interface PageableResponse {
    }

    interface PageableTypedResponse<D extends Data> extends PageableResponse {

        PageableMeta getMeta();

        void setMeta(PageableMeta meta);

        List<D> getData();

        void setData(List<D> data) throws ClassCastException;

        List<Object> getIncluded();

        void setIncluded(List<Object> included);

        Links getLinks();

        void setLinks(Links links);

        Class<D> getResponseDataClass();

        ResourceObjectType getResponseDataType();
    }
}