package com.dss.tennis.tournament.tables.converter.modelmapper;

import com.dss.tennis.tournament.tables.converter.*;
import com.dss.tennis.tournament.tables.converter.patch.PlayerDtoPathApplier;
import com.dss.tennis.tournament.tables.converter.patch.TournamentDtoPathApplier;
import com.dss.tennis.tournament.tables.converter.v2.request.*;
import com.dss.tennis.tournament.tables.converter.v2.response.*;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.EliminationContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestRequest.UpdateContestScoreRequest;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestResponseData;
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
import com.dss.tennis.tournament.tables.model.request.CreateScore;
import com.dss.tennis.tournament.tables.model.request.CreateTeam;
import com.dss.tennis.tournament.tables.model.request.PatchPlayer;
import com.dss.tennis.tournament.tables.model.request.PatchTournament;
import com.dss.tennis.tournament.tables.model.response.v1.*;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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

        modelMapper.addMappings(new PropertyMap<TournamentDTO, GetTournament>() {
            @Override
            protected void configure() {
                skip(destination.getType());
                skip(destination.getContests());
            }
        });

        // converters
        modelMapper.createTypeMap(SingleContestDTO.class, GetContest.class)
                .setPostConverter(new SingleContestDtoToGetResponseConverter());
        modelMapper.createTypeMap(DoubleContestDTO.class, GetContest.class)
                .setPostConverter(new DoubleContestDtoToGetResponseConverter());
        modelMapper.createTypeMap(PlayerDTO.class, ResourceObject.class)
                .setPostConverter(new PlayerDtoToResourceObjectConverter());
        modelMapper.createTypeMap(PlayerDTO.class, GetPlayer.class)
                .setPostConverter(new PlayerDtoToGetPlayerConverter());
        modelMapper.createTypeMap(PageableDTO.class, GetPageable.class)
                .setPostConverter(new PageableDtoToGetPageableConverter(modelMapper));
        modelMapper.createTypeMap(TeamDTO.class, GetTeam.class)
                .setPostConverter(new TeamDtoToGetTeamConverter(modelMapper));
        modelMapper.createTypeMap(CreateTeam.class, TeamDTO.class)
                .setPostConverter(new CreateTeamToTeamDtoConverter(ResourceObjectType.PLAYER.value));
        modelMapper.createTypeMap(String.class, ResourceObjectType.class)
                .setConverter(new ResourceObjectTypeStringToEnumConverter());
        modelMapper.createTypeMap(CreateScore.class, ScoreDTO.class)
                .setConverter(new CreateScoreToScoreDtoConverter(ResourceObjectType.SET_SCORE.value));
        modelMapper.createTypeMap(CreateScore.class, ScorePatchDTO.class)
                .setConverter(new CreateScoreToScorePatchDtoConverter(ResourceObjectType.SET_SCORE.value));
        modelMapper.createTypeMap(EliminationContestDTO.class, GetEliminationContest.class)
                .setPostConverter(new EliminationContestDtoToGetObjectConverter(modelMapper));
        modelMapper.getTypeMap(TournamentDTO.class, GetTournament.class)
                .setPostConverter(new TournamentDtoToGetObjectConverter(modelMapper));

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
                .setConverter(new SingleContestDtoToContestResponseDataConverter(modelMapper));
        modelMapper.createTypeMap(DoubleContestDTO.class, ContestInfoResponseData.class)
                .setConverter(new DoubleContestDtoToContestInfoResponseDataConverter(extraString));
        modelMapper.createTypeMap(DoubleContestDTO.class, ContestResponseData.class)
                .setConverter(new DoubleContestDtoToContestResponseDataConverter(modelMapper));
        modelMapper.createTypeMap(PageableDTO.class, PageableTournamentResponse.class)
                .setConverter(new PageableDtoToPageableTournamentResponse(modelMapper));
        modelMapper.createTypeMap(CreteTournamentRequest.class, TournamentDTO.class)
                .setConverter(new CreateTournamentRequestToTournamentDtoConverter());
        modelMapper.createTypeMap(UpdateTournamentRequest.class, PatchTournament.class)
                .setConverter(new UpdateTournamentRequestToPatchTournamentConverter());
        modelMapper.createTypeMap(EnrollTournamentParticipantRequest.class, List.class)
                .setConverter(new EnrollTournamentParticipantRequestToResourceObjectListConverter());
        modelMapper.createTypeMap(EliminationContestDTO.class, ContestResponseData.class)
                .setConverter(new EliminationContestDtoToContestResponseDataConverter(modelMapper));
        modelMapper.createTypeMap(UpdateContestScoreRequest.class, ContestScorePatchDTO.class)
                .setConverter(new UpdateContestRequestToScorePatchConverter());
    }
}
