package ua.com.dss.tennis.tournament.api.model.definitions.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.Meta.PageableMeta;
import ua.com.dss.tennis.tournament.api.model.definitions.Pageable.PageableTypedResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentResponse.TournamentResponseData;

import java.util.List;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TOURNAMENT;

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
