package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.modelmapper.ModelMapperFactory;
import org.springframework.stereotype.Service;

@Service
public class PatchApplierHelper {

    public <P, D> D applyPatch(P patch, D destination) {
        ModelMapperFactory.getStatelessInstance().map(patch, destination);
        return destination;
    }
}
