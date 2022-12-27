package ua.com.dss.tennis.tournament.api.model.definitions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleResourceObject {
    private Integer id;
    private String type;
}
