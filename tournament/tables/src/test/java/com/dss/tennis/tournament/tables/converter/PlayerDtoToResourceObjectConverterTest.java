package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;

@ExtendWith(MockitoExtension.class)
class PlayerDtoToResourceObjectConverterTest {

    private static final Integer PLAYER_ID = 1;
    private static final String PLAYER_FIRST_NAME = "FirstNameOne";
    private static final String PLAYER_LAST_NAME = "LastNameOne";

    @Spy
    private MappingContext<PlayerDTO, ResourceObject> mappingContextSpy;

    PlayerDtoToResourceObjectConverter testInstance = new PlayerDtoToResourceObjectConverter();

//    @Test
//    public void shouldConvertContestDtoToGetResponseWithScore() {
//        PlayerDTO playerOne = new PlayerDTO(PLAYER_ID, PLAYER_FIRST_NAME, PLAYER_LAST_NAME);
//        when(mappingContextSpy.getSource()).thenReturn(playerOne);
//        ResourceObject resourceObject = new ResourceObject();
//        when(mappingContextSpy.getDestination()).thenReturn(resourceObject);
//
//        ResourceObject result = testInstance.convert(mappingContextSpy);
//
//        Assertions.assertAll(
//                () -> assertNotNull(result),
//                () -> assertEquals(result.getType(), ResourceObjectType.PLAYER.value),
//                () -> assertTrue(result.getAttributes() instanceof GetPlayer),
//                () -> assertNull(((GetPlayer) result.getAttributes()).getId()),
//                () -> assertEquals(((GetPlayer) result.getAttributes()).getFirstName(), PLAYER_FIRST_NAME),
//                () -> assertEquals(((GetPlayer) result.getAttributes()).getLastName(), PLAYER_LAST_NAME)
//        );
//    }
}
