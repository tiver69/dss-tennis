package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.modelmapper.ModelMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatchApplierHelper {

    @Autowired
    private ModelMapperFactory modelMapperFactory;

    public <P, D> D applyPatch(P patch, D destination) {
        modelMapperFactory.getCustomizedModelMapper().map(patch, destination);
        return destination;
    }
}
