package com.dss.tennis.tournament.tables.model.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractSequentialDTO {
    private Integer sequenceNumber;
}
