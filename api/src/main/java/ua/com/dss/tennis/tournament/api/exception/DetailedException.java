package ua.com.dss.tennis.tournament.api.exception;

import lombok.Getter;
import lombok.Setter;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class DetailedException extends RuntimeException {

    private Set<ErrorDataDTO> errors = new HashSet<>();

    public DetailedException(ErrorKey error) {
        this(error, null);
    }

    public DetailedException(ErrorKey error, Object detailParameter) {
        this(error, detailParameter != null ? detailParameter.toString() : null);
    }

    public DetailedException(ErrorKey error, String detailParameter) {
        this.errors.add(new ErrorDataDTO(error, detailParameter, null));
    }

    public DetailedException(ErrorKey error, String detailParameter, byte sequentialNumber) {
        this.errors.add(new ErrorDataDTO(error, detailParameter, sequentialNumber));
    }

    public DetailedException(Set<ErrorDataDTO> errors) {
        this.errors = errors;
    }

    public boolean contains(ErrorKey searchedError) {
        return errors.stream().anyMatch(errorDataDTO -> errorDataDTO.getErrorKey() == searchedError);
    }

    @Override
    public String getMessage() {
        return errors.stream()
                .map(errorDto -> String.format("%s[%s]", errorDto.getErrorKey().toString(), errorDto
                        .getPointer() != null ? errorDto.getPointer() : errorDto.getDetailParameter()))
                .collect(Collectors.joining(", "));
    }
}
