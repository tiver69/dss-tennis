package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetDoubleContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetSingleContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetTournament;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class TournamentDtoToGetResponseConverter implements Converter<TournamentDTO, GetTournament> {

    private ModelMapper modelMapper;

    @Override
    public GetTournament convert(MappingContext<TournamentDTO, GetTournament> context) {
        TournamentDTO tournamentDto = context.getSource();
        GetTournament getTournament = context.getDestination();

        Class<? extends GetContest> getContestType = tournamentDto.getParticipantType() == ParticipantType.SINGLE ?
                GetSingleContest.class : GetDoubleContest.class;
        List<GetContest> contests = tournamentDto.getContests().stream()
                .map(contest -> modelMapper.map(contest, getContestType)).collect(Collectors.toList());

        getTournament.setContests(contests);
        return getTournament;
    }
}
