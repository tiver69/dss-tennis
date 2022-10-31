package com.dss.tennis.tournament.tables.model.definitions;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;

public interface PageableResponse {

    Meta getMeta();

    Object getData();

    Links getLinks();

    Class getResponseDataClass();

    ResourceObjectType getResponseDataType();
}
