package com.dss.tennis.tournament.tables.model.definitions;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;

import java.util.List;

public interface PageableResponse {

    Meta getMeta();

    void setMeta(Meta meta);

    Object getData();

    void setData(Object data) throws ClassCastException;

    List<Object> getIncluded();

    void setIncluded(List<Object> included);

    Links getLinks();

    void setLinks(Links links);

    Class getResponseDataClass();

    ResourceObjectType getResponseDataType();
}
