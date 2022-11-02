package com.dss.tennis.tournament.tables.model.definitions.player;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.Meta;
import com.dss.tennis.tournament.tables.model.definitions.PageableResponse;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageablePlayerResponse implements PageableResponse {

    private Meta meta;
    private List<PlayerResponseData> data;
    private Links links;

    @Override
    public void setData(Object data) throws ClassCastException {
        this.data = (List<PlayerResponseData>) data;
    }

    @Override
    public List<Object> getIncluded() {
        return null;
    }

    @Override
    public void setIncluded(List<Object> included) {
        //No included section
    }

    @Override
    @JsonIgnore
    public Class getResponseDataClass() {
        return PlayerResponseData.class;
    }

    @Override
    @JsonIgnore
    public ResourceObjectType getResponseDataType() {
        return ResourceObjectType.PLAYER;
    }
}
