package ua.com.dss.tennis.tournament.api.model.definitions.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.Meta.PageableMeta;
import ua.com.dss.tennis.tournament.api.model.definitions.Pageable.PageableTypedResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType;
import ua.com.dss.tennis.tournament.api.model.definitions.team.TeamResponse.TeamResponseData;

import java.util.List;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TEAM;

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
