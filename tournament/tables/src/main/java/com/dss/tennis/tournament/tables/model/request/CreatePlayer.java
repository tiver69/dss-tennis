package com.dss.tennis.tournament.tables.model.request;

import com.dss.tennis.tournament.tables.model.db.v1.LeadingHand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Deprecated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlayer {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer experienceYear;
    private LeadingHand leadingHand;
}
