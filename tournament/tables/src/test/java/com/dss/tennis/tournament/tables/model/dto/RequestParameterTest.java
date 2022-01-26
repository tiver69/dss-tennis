package com.dss.tennis.tournament.tables.model.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestParameterTest {

    @Test
    public void shouldCopyRequestParameter() {
        RequestParameter source = new RequestParameter();
        RequestParameter destination = new RequestParameter();
        source.setIncludeContests(true);

        source.copyRequestParameterTo(destination);

        Assertions.assertAll(
                () -> assertTrue(destination.isIncludeContests())
        );
    }

    @Test
    public void shouldResetStateToDefault() {
        RequestParameter testInstance = new RequestParameter();
        testInstance.setIncludeContests(true);

        testInstance.resetStateToDefault();

        Assertions.assertAll(
                () -> assertFalse(testInstance.isIncludeContests())
        );
    }
}