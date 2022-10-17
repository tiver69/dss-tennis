package com.dss.tennis.tournament.tables.model.definitions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Links {
    private String first;
    private String last;
    private String prev;
    private String self;
    private String next;
}
