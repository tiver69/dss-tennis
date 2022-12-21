package com.dss.tennis.tournament.tables.model.db.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer experienceYear;
    private LeadingHand leadingHand;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "first_name", nullable = false, length = 45)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = false, length = 45)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "birth_date", nullable = false)
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Basic
    @Column(name = "experience_year", nullable = false)
    public Integer getExperienceYear() {
        return experienceYear;
    }

    public void setExperienceYear(Integer experienceYear) {
        this.experienceYear = experienceYear;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "leading_hand", nullable = false)
    public LeadingHand getLeadingHand() {
        return leadingHand;
    }

    public void setLeadingHand(LeadingHand leadingHand) {
        this.leadingHand = leadingHand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (id != player.id) return false;
        if (firstName != null ? !firstName.equals(player.firstName) : player.firstName != null) return false;
        if (lastName != null ? !lastName.equals(player.lastName) : player.lastName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", experienceYear=" + experienceYear +
                ", leadingHand=" + leadingHand +
                '}';
    }
}
