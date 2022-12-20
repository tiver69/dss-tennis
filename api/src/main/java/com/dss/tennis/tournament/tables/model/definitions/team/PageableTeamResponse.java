package com.dss.tennis.tournament.tables.model.definitions.team;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.Meta.PageableMeta;
import com.dss.tennis.tournament.tables.model.definitions.Pageable.PageableTypedResponse;
import com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TEAM;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableTeamResponse implements PageableTypedResponse<TeamResponseData> {

    private PageableMeta meta;
    private List<TeamResponseData> data;
    private List<Object> included;
    private Links links;

    @Override
    public void setData(List<TeamResponseData> data) throws ClassCastException {
        this.data = data;
    }

    @Override
    @JsonIgnore
    public Class<TeamResponseData> getResponseDataClass() {
        return TeamResponseData.class;
    }

    @Override
    @JsonIgnore
    public ResourceObjectType getResponseDataType() {
        return TEAM;
    }
}
