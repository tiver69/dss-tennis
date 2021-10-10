package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TournamentHelperTest {

    @Mock
    private TournamentRepository tournamentRepository;
    @Spy
    private CreateTournamentDTO createTournamentDTO;

    @InjectMocks
    private TournamentHelper testInstance;

    @Test
    public void shouldCreateNewTournament() {
        testInstance.createNewTournament(createTournamentDTO);

        verify(tournamentRepository).save(any(Tournament.class));
    }
}