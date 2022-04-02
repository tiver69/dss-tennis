package com.dss.tennis.tournament.tables.model.request;

import com.dss.tennis.tournament.tables.model.db.v1.LeadingHand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchPlayer {
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<LocalDate> birthDate;
    private Optional<Integer> experienceYear;
    private Optional<LeadingHand> leadingHand;
}