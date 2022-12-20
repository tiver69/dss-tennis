package com.dss.tennis.tournament.tables.model.definitions.player;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.Meta.PageableMeta;
import com.dss.tennis.tournament.tables.model.definitions.Pageable.PageableTypedResponse;
import com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.PLAYER;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageablePlayerResponse implements PageableTypedResponse<PlayerResponseData> {

    private PageableMeta meta;
    private List<PlayerResponseData> data;
    private Links links;

    @Override
    public void setData(List<PlayerResponseData> data) throws ClassCastException {
        this.data = data;
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
    public Class<PlayerResponseData> getResponseDataClass() {
        return PlayerResponseData.class;
    }

    @Override
    @JsonIgnore
    public ResourceObjectType getResponseDataType() {
        return PLAYER;
    }
}
