package com.dss.tennis.tournament.tables.exception;

import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class DetailedException extends RuntimeException {

    private Set<ErrorDataDTO> errors = new HashSet<>();

    public DetailedException(ErrorConstants error) {
        this(error, null);
    }

    public DetailedException(ErrorConstants error, Object detailParameter) {
        this(error, detailParameter != null ? detailParameter.toString() : null);
    }

    public DetailedException(ErrorConstants error, String detailParameter) {
        this.errors.add(new ErrorDataDTO(error, detailParameter, null));
    }

    public DetailedException(ErrorConstants error, String detailParameter, byte sequentialNumber) {
        this.errors.add(new ErrorDataDTO(error, detailParameter, sequentialNumber));
    }

    public DetailedException(Set<ErrorDataDTO> errors) {
        this.errors = errors;
    }

    public boolean contains(ErrorConstants searchedError) {
        return errors.stream().anyMatch(errorDataDTO -> errorDataDTO.getErrorConstant() == searchedError);
    }

    @Override
    public String getMessage() {
        return errors.stream()
                .map(errorDto -> String.format("%s[%s]", errorDto.getErrorConstant().toString(), errorDto
                        .getPointer() != null ? errorDto.getPointer() : errorDto.getDetailParameter()))
                .collect(Collectors.joining(", "));
    }
}
