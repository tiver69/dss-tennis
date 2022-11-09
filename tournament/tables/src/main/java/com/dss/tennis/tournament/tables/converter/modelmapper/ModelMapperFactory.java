package com.dss.tennis.tournament.tables.converter.modelmapper;

import com.dss.tennis.tournament.tables.converter.patch.PlayerDtoPathApplier;
import com.dss.tennis.tournament.tables.converter.patch.TournamentDtoPathApplier;
import com.dss.tennis.tournament.tables.converter.v2.dto.DoubleContestToDoubleContestDtoConverter;
import com.dss.tennis.tournament.tables.converter.v2.dto.EliminationContestToEliminationContestDtoConverter;
import com.dss.tennis.tournament.tables.converter.v2.dto.SingleContestToSingleContestDtoConverter;
import com.dss.tennis.tournament.tables.converter.v2.request.*;
import com.dss.tennis.tournament.tables.converter.v2.response.*;
import com.dss.tennis.tournament.tables.converter.v2.response.pageable.PageableDtoToPageablePlayerResponse;
import com.dss.tennis.tournament.tables.converter.v2.response.pageable.PageableDtoToPageableTeamResponse;
import com.dss.tennis.tournament.tables.converter.v2.response.pageable.PageableDtoToPageableTournamentResponse;
import com.dss.tennis.tournament.tables.model.db.v2.DoubleContest;
import com.dss.tennis.tournament.tables.model.db.v2.EliminationContest;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.EliminationContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestRequest.UpdateContestScoreRequest;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestResponseData;
import com.dss.tennis.tournament.tables.model.definitions.player.PageablePlayerResponse;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.CretePlayerRequest;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.UpdatePlayerRequest;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.dss.tennis.tournament.tables.model.definitions.team.CreateTeamRequest;
import com.dss.tennis.tournament.tables.model.definitions.team.PageableTeamResponse;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseData;
import com.dss.tennis.tournament.tables.model.definitions.tournament.EnrollTournamentParticipantRequest;
import com.dss.tennis.tournament.tables.model.definitions.tournament.PageableTournamentResponse;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentRequest.CreteTournamentRequest;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentRequest.UpdateTournamentRequest;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseData;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.request.PatchPlayer;
import com.dss.tennis.tournament.tables.model.request.PatchTournament;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelMapperFactory {

    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    public ModelMapper getCustomizedModelMapper() {
        return getCustomizedModelMapper(null);
    }

    public ModelMapper getCustomizedModelMapper(String extraString) {
        ModelMapper modelMapper = getModelMapper();

        applyConvertersForV2(modelMapper, extraString);

        //patch appliers
        modelMapper.createTypeMap(PatchPlayer.class, PlayerDTO.class)
                .setConverter(new PlayerDtoPathApplier());
        modelMapper.createTypeMap(PatchTournament.class, TournamentDTO.class)
                .setConverter(new TournamentDtoPathApplier());
        return modelMapper;
    }

    private void applyConvertersForV2(ModelMapper modelMapper, String extraString) {
        modelMapper.createTypeMap(PlayerDTO.class, PlayerResponseData.class)
                .setConverter(new PlayerDtoToPlayerResponseDataConverter());
        modelMapper.createTypeMap(UpdatePlayerRequest.class, PatchPlayer.class)
                .setConverter(new UpdatePlayerRequestToPatchPlayerConverter());
        modelMapper.createTypeMap(CretePlayerRequest.class, PlayerDTO.class)
                .setConverter(new CreatePlayerRequestToPlayerDtoConverter());
        modelMapper.createTypeMap(PageableDTO.class, PageablePlayerResponse.class)
                .setConverter(new PageableDtoToPageablePlayerResponse(modelMapper));
        modelMapper.createTypeMap(TeamDTO.class, TeamResponseData.class)
                .setConverter(new TeamDtoToTeamResponseDataConverter(modelMapper));
        modelMapper.createTypeMap(PageableDTO.class, PageableTeamResponse.class)
                .setConverter(new PageableDtoToPageableTeamResponse(modelMapper));
        modelMapper.createTypeMap(CreateTeamRequest.class, TeamDTO.class)
                .setConverter(new CreateTeamRequestToTeamDtoConverter());
        modelMapper.createTypeMap(TournamentDTO.class, TournamentResponseData.class)
                .setConverter(new TournamentDtoToTournamentResponseDataConverter(modelMapper));
        modelMapper.createTypeMap(EliminationContestDTO.class, EliminationContestInfoResponseData.class)
                .setConverter(new EliminationContestDtoToEliminationContestInfoResponseDataConverter(extraString));
        modelMapper.createTypeMap(SingleContestDTO.class, ContestInfoResponseData.class)
                .setConverter(new SingleContestDtoToContestInfoResponseDataConverter(extraString));
        modelMapper.createTypeMap(SingleContestDTO.class, ContestResponseData.class)
                .setConverter(new SingleContestDtoToContestResponseDataConverter(extraString));
        modelMapper.createTypeMap(DoubleContestDTO.class, ContestInfoResponseData.class)
                .setConverter(new DoubleContestDtoToContestInfoResponseDataConverter(extraString));
        modelMapper.createTypeMap(DoubleContestDTO.class, ContestResponseData.class)
                .setConverter(new DoubleContestDtoToContestResponseDataConverter(extraString));
        modelMapper.createTypeMap(PageableDTO.class, PageableTournamentResponse.class)
                .setConverter(new PageableDtoToPageableTournamentResponse(modelMapper));
        modelMapper.createTypeMap(CreteTournamentRequest.class, TournamentDTO.class)
                .setConverter(new CreateTournamentRequestToTournamentDtoConverter());
        modelMapper.createTypeMap(UpdateTournamentRequest.class, PatchTournament.class)
                .setConverter(new UpdateTournamentRequestToPatchTournamentConverter());
        modelMapper.createTypeMap(EnrollTournamentParticipantRequest.class, List.class)
                .setConverter(new EnrollTournamentParticipantRequestToResourceObjectListConverter());
        modelMapper.createTypeMap(EliminationContestDTO.class, ContestResponseData.class)
                .setConverter(new EliminationContestDtoToContestResponseDataConverter(extraString));
        modelMapper.createTypeMap(UpdateContestScoreRequest.class, ContestScorePatchDTO.class)
                .setConverter(new UpdateContestScoreRequestToScorePatchConverter());

        modelMapper.createTypeMap(SingleContest.class, SingleContestDTO.class)
                .setConverter(new SingleContestToSingleContestDtoConverter());
        modelMapper.createTypeMap(DoubleContest.class, DoubleContestDTO.class)
                .setConverter(new DoubleContestToDoubleContestDtoConverter());
        modelMapper.createTypeMap(EliminationContest.class, EliminationContestDTO.class)
                .setConverter(new EliminationContestToEliminationContestDtoConverter());
    }
}
