package com.dss.tennis.tournament.tables.model.definitions.tournament;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.Meta.PageableMeta;
import com.dss.tennis.tournament.tables.model.definitions.Pageable.PageableTypedResponse;
import com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TOURNAMENT;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableTournamentResponse implements PageableTypedResponse<TournamentResponseData> {

    private PageableMeta meta;
    private List<TournamentResponseData> data;
    private List<Object> included;
    private Links links;

    @Override
    public void setData(List<TournamentResponseData> data) throws ClassCastException {
        this.data = data;
    }

    @Override
    @JsonIgnore
    public Class<TournamentResponseData> getResponseDataClass() {
        return TournamentResponseData.class;
    }

    @Override
    @JsonIgnore
    public ResourceObjectType getResponseDataType() {
        return TOURNAMENT;
    }
}
