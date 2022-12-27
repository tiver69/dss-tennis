package ua.com.dss.tennis.tournament.api.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.*;

@Component
public class RequestParameterHelper {

    public static String PARAMETER_URL_SEPARATOR = "?";

    private final Map<ResourceObjectType, List<Order>> pageableSortParameters = new HashMap<>();

    @PostConstruct
    protected void initialize() {
        pageableSortParameters.put(TOURNAMENT, List.of(new Order(DESC, "beginningDate")));
        pageableSortParameters.put(PLAYER, List
                .of(new Order(ASC, "lastName"), new Order(ASC, "firstName")));
        pageableSortParameters.put(TEAM, List
                .of(new Order(ASC, "playerOneId"), new Order(ASC, "playerTwoId")));
    }

    public Pageable populatePageableRequestParameter(int page, int pageSize, ResourceObjectType resourceType) {
        return PageRequest.of(page, pageSize, Sort.by(pageableSortParameters.get(resourceType)));
    }
}
