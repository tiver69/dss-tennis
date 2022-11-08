package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Component
public class RequestParameterHelper {

    public static String PARAMETER_URL_SEPARATOR = "?";

    private final Map<ResourceObjectType, List<Order>> pageableSortParameters = new HashMap<>();

    @PostConstruct
    protected void initialize() {
        pageableSortParameters.put(ResourceObjectType.TOURNAMENT, List
                .of(new Order(DESC, "status"), new Order(DESC, "beginningDate")));
        pageableSortParameters.put(ResourceObjectType.PLAYER, List
                .of(new Order(ASC, "lastName"), new Order(ASC, "firstName")));
        pageableSortParameters.put(ResourceObjectType.TEAM, List
                .of(new Order(ASC, "playerOneId"), new Order(ASC, "playerTwoId")));
    }

    public Pageable populatePageableRequestParameter(int page, int pageSize, ResourceObjectType resourceType) {
        return PageRequest.of(page, pageSize, Sort.by(pageableSortParameters.get(resourceType)));
    }
}
