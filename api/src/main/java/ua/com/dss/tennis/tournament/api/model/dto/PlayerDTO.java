package ua.com.dss.tennis.tournament.api.model.dto;

import lombok.*;
import ua.com.dss.tennis.tournament.api.model.db.v1.LeadingHand;
import ua.com.dss.tennis.tournament.api.validator.constraint.anotation.Required;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {

    public PlayerDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PlayerDTO(Integer id) {
        this.id = id;
    }

    private int id;

    @Required(message = "PLAYER_FIRST_NAME_EMPTY")
    private String firstName;
    @Required(message = "PLAYER_LAST_NAME_EMPTY")
    private String lastName;

    private LocalDate birthDate;
    private Integer experienceYear;
    private LeadingHand leadingHand;

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerDTO)) return false;
        final PlayerDTO other = (PlayerDTO) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$firstName = this.getFirstName();
        final Object other$firstName = other.getFirstName();
        if (this$firstName == null ? other$firstName != null : !this$firstName.equals(other$firstName)) return false;
        final Object this$lastName = this.getLastName();
        final Object other$lastName = other.getLastName();
        return this$lastName == null ? other$lastName != null : !this$lastName.equals(other$lastName);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayerDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final Object $firstName = this.getFirstName();
        result = result * PRIME + ($firstName == null ? 43 : $firstName.hashCode());
        final Object $lastName = this.getLastName();
        return result * PRIME + ($lastName == null ? 43 : $lastName.hashCode());
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", experienceYear=" + experienceYear +
                ", leadingHand=" + leadingHand +
                '}';
    }
}
