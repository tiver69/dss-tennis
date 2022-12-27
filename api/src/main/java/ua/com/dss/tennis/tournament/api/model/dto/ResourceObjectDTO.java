package ua.com.dss.tennis.tournament.api.model.dto;

import lombok.*;
import ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceObjectDTO extends AbstractSequentialDTO {

    private int id;
    private ResourceObjectType type;
}
