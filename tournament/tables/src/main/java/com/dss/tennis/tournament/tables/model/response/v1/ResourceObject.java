package com.dss.tennis.tournament.tables.model.response.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Deprecated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceObject<T> {

    private Integer id;
    private String type;
    private T attributes;

    @JsonIgnore
    public boolean isInitializedEmpty() {
        return id == null && type == null && attributes == null;
    }
}
