package ua.com.dss.tennis.tournament.api.model.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractSequentialDTO {
    private Byte sequenceNumber;
}
