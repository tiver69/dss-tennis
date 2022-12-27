package ua.com.dss.tennis.tournament.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponseDTO<D> {

    private D data;
    private Set<ErrorDataDTO> warnings;
}
