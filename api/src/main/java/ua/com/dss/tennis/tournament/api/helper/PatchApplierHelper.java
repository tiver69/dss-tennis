package ua.com.dss.tennis.tournament.api.helper;

import org.springframework.stereotype.Service;
import ua.com.dss.tennis.tournament.api.converter.ModelMapperFactory;

@Service
public class PatchApplierHelper {

    public <P, D> D applyPatch(P patch, D destination) {
        ModelMapperFactory.getStatelessInstance().map(patch, destination);
        return destination;
    }
}
