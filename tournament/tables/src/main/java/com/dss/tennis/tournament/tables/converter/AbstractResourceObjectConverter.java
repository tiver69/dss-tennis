package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.*;

@AllArgsConstructor
public abstract class AbstractResourceObjectConverter {

    private final String allowedResourceType;

    protected Set<ErrorDataDTO> validateResourceObjectIdMappingWithId(ResourceObject resourceObject,
                                                                      String customPointer) {
        Set<ErrorDataDTO> errors = new HashSet<>();
        if (validateBaseResourceObjectMapping(resourceObject, customPointer, errors)) return errors;
        if (resourceObject.getId() == null)
            errors.add(ErrorDataDTO.builder().errorConstant(RESOURCE_OBJECT_ID_EMPTY).pointer(customPointer)
                    .build());
        return errors;
    }

    protected Set<ErrorDataDTO> validateResourceObjectMappingWitAttributes(ResourceObject resourceObject,
                                                                           String customPointer) {
        Set<ErrorDataDTO> errors = new HashSet<>();
        if (validateBaseResourceObjectMapping(resourceObject, customPointer, errors)) return errors;
        if (resourceObject.getAttributes() == null)
            errors.add(ErrorDataDTO.builder().errorConstant(RESOURCE_OBJECT_ATTRIBUTES_EMPTY).pointer(customPointer)
                    .build());
        return errors;
    }

    private boolean validateBaseResourceObjectMapping(ResourceObject resourceObject, String customPointer,
                                                      Set<ErrorDataDTO> errors) {
        if (resourceObject == null) return true;
        if (resourceObject.isInitializedEmpty()) {
            errors.add(ErrorDataDTO.builder().errorConstant(RESOURCE_OBJECT_EMPTY).pointer(customPointer).build());
            return true;
        }
        if (!allowedResourceType.equals(resourceObject.getType()))
            errors.add(ErrorDataDTO.builder().errorConstant(RESOURCE_OBJECT_TYPE_FORBIDDEN).pointer(customPointer)
                    .build());
        return false;
    }
}
