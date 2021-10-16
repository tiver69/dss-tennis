package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponseDTO<D> {

    private D data;
    private List<ErrorData> warnings;
}
