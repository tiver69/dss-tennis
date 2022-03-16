package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceObjectDTO extends AbstractSequentialDTO{

    private int id;
    private ResourceObjectType type;
}
