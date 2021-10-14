package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.db.v1.Contest;
import com.dss.tennis.tournament.tables.model.db.v1.Score;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContestToDtoConverterTest {

    @Mock
    private ModelMapper modelMapperMock;
    @Spy
    private MappingContext<Contest, ContestDTO> mappingContextSpy;
    @Spy
    private Contest contestSpy;
    @Spy
    private Score scoreSpy;
    @Spy
    private ContestDTO contestDtoSpy;

    @InjectMocks
    private ContestToDtoConverter testInstance;

    @Test
    public void shouldConvertContestToDto() {
        when(mappingContextSpy.getSource()).thenReturn(contestSpy);
        when(contestSpy.getScore()).thenReturn(scoreSpy);
        when(mappingContextSpy.getDestination()).thenReturn(contestDtoSpy);
        ContestDTO result = testInstance.convert(mappingContextSpy);

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result, contestDtoSpy)
        );
        verify(modelMapperMock).map(scoreSpy, contestDtoSpy);
    }
}