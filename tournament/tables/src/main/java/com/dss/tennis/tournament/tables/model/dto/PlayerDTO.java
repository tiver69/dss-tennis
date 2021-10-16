package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.validator.anotation.Required;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO extends AbstractSequentialDTO {

    public PlayerDTO(String firstName, String lastName, Byte sequenceNumber) {
        super(sequenceNumber);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PlayerDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private int id;
    @Required(message = "PLAYER_FIRST_NAME_EMPTY")
    private String firstName;
    @Required(message = "PLAYER_LAST_NAME_EMPTY")
    private String lastName;


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
        if (this$lastName == null ? other$lastName != null : !this$lastName.equals(other$lastName)) return false;
        return this.getSequenceNumber() == null ? other.getSequenceNumber() == null : this.getSequenceNumber()
                .equals(other.getSequenceNumber());
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
        result = result * PRIME + ($lastName == null ? 43 : $lastName.hashCode());
        return result * PRIME + (this.getSequenceNumber() == null ? 43 : this.getSequenceNumber().hashCode());
    }

    public String toString() {
        return "PlayerDTO(id=" + this.getId() + ", firstName=" + this.getFirstName() + ", lastName=" + this
                .getLastName() + ", sequenceNumber =" + this.getSequenceNumber() + ")";
    }
}
