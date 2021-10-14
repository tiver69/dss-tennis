package com.dss.tennis.tournament.tables.converter.modelmapper;

import com.dss.tennis.tournament.tables.model.db.v1.Contest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

class ModelMapperFactoryTest {

    private ModelMapperFactory testIntense = new ModelMapperFactory();

    @Test
    public void shouldReturnModelMapper() {
        ModelMapper result = testIntense.getModelMapper();

        Assertions.assertNotNull(result);
    }

    @Test
    public void shouldReturnCustomizedModelMapper() {
        ModelMapper result = testIntense.getCustomizedModelMapper();

        Assertions.assertAll(
                () -> assertNotNull(result.getTypeMap(Contest.class, ContestDTO.class).getPostConverter()),
                () -> assertNotNull(result.getTypeMap(ContestDTO.class, GetContest.class).getPostConverter())
        );
    }
}