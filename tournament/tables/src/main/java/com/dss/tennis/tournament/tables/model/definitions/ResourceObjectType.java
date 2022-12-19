package com.dss.tennis.tournament.tables.model.definitions;

import com.dss.tennis.tournament.tables.exception.DetailedException;

import java.util.Arrays;
import java.util.Optional;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.ErrorKey.UNSUPPORTED_RESOURCE_TYPE;

public enum ResourceObjectType {
    PLAYER("player", "/participants/players"),
    TEAM("team", "/participants/teams"),
    TOURNAMENT("tournament", "/tournaments"),
    CONTEST("contest", "/tournaments/%d/contests/%d", null),
    CONTEST_INFO("contestInfo", "/tournaments/%d/contests/%d", null),
    ELIMINATION_CONTEST_INFO("eliminationContestInfo", "/tournaments/%d/contests/%d", null),
    SET_SCORE("setScore", null, null);

    public final String value;
    public final String selfLinkFormat;
    public final String pageableLinkFormat;

    ResourceObjectType(String value, String basicLink) {
        this(value, basicLink + "/%d", basicLink + "?page=%d&pageSize=%d");
    }

    ResourceObjectType(String value, String selfLinkFormat, String pageableLinkFormat) {
        this.value = value;
        this.selfLinkFormat = selfLinkFormat;
        this.pageableLinkFormat = pageableLinkFormat;
    }

    public static ResourceObjectType fromStringValue(String value) {
        Optional<ResourceObjectType> result = Arrays.stream(ResourceObjectType.values())
                .filter(tt -> value.equals(tt.value)).findFirst();
        return result.orElseThrow(() -> new DetailedException(UNSUPPORTED_RESOURCE_TYPE, value));
    }
}
