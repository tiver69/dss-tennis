package ua.com.dss.tennis.tournament.api.model.definitions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Links {
    private String first;
    private String prev;
    private String self;
    private String next;
    private String last;
}
