package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetEliminationContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetTournament;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TournamentDtoToGetObjectConverter implements Converter<TournamentDTO, GetTournament> {

    private ModelMapper modelMapper;

    @Override
    public GetTournament convert(MappingContext<TournamentDTO, GetTournament> context) {
        TournamentDTO sourceTournament = context.getSource();
        GetTournament destinationTournament = context.getDestination();

        if (sourceTournament.getContests() == null) return destinationTournament;
        if (sourceTournament.getTournamentType() == TournamentType.ROUND) {
            List<ContestDTO> contests = (List) sourceTournament.getContests();
            destinationTournament
                    .setContests(contests.stream().map(contest -> modelMapper.map(contest, GetContest.class))
                            .collect(Collectors.toList()));
        } else {
            EliminationContestDTO contests = (EliminationContestDTO) sourceTournament.getContests();
            destinationTournament.setContests(modelMapper.map(contests, GetEliminationContest.class));
        }
        return destinationTournament;
    }
}
