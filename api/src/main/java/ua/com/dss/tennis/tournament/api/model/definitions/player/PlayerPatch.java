package ua.com.dss.tennis.tournament.api.model.definitions.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.dss.tennis.tournament.api.model.db.v1.LeadingHand;

import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerPatch {
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<LocalDate> birthDate;
    private Optional<Integer> experienceYear;
    private Optional<LeadingHand> leadingHand;
}
