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

import java.util.List;

public class ModelMapperFactory {

    private static volatile ModelMapper instance;
    private static final Object mutex = new Object();

    public static ModelMapper getStatelessInstance() {
        ModelMapper result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = getStatelessModelMapper();
            }
        }
        return result;
    }

    public static ModelMapper getStatefulInstance(Integer extraInteger) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(EliminationContestDTO.class, EliminationContestInfoResponseData.class)
                .setConverter(new EliminationContestDtoToEliminationContestInfoResponseDataConverter(extraInteger));
        modelMapper.createTypeMap(SingleContestDTO.class, ContestInfoResponseData.class)
                .setConverter(new SingleContestDtoToContestInfoResponseDataConverter(extraInteger));
        modelMapper.createTypeMap(SingleContestDTO.class, ContestResponseData.class)
                .setConverter(new SingleContestDtoToContestResponseDataConverter(extraInteger));
        modelMapper.createTypeMap(DoubleContestDTO.class, ContestInfoResponseData.class)
                .setConverter(new DoubleContestDtoToContestInfoResponseDataConverter(extraInteger));
        modelMapper.createTypeMap(DoubleContestDTO.class, ContestResponseData.class)
                .setConverter(new DoubleContestDtoToContestResponseDataConverter(extraInteger));
        modelMapper.createTypeMap(EliminationContestDTO.class, ContestResponseData.class)
                .setConverter(new EliminationContestDtoToContestResponseDataConverter(extraInteger));

        return modelMapper;
    }

    private static ModelMapper getStatelessModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        applyConvertersForV2(modelMapper);

        //patch appliers
        modelMapper.createTypeMap(PatchPlayer.class, PlayerDTO.class)
                .setConverter(new PlayerDtoPathApplier());
        modelMapper.createTypeMap(PatchTournament.class, TournamentDTO.class)
                .setConverter(new TournamentDtoPathApplier());
        return modelMapper;
    }

    private static void applyConvertersForV2(ModelMapper modelMapper) {
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
        modelMapper.createTypeMap(PageableDTO.class, PageableTournamentResponse.class)
                .setConverter(new PageableDtoToPageableTournamentResponse(modelMapper));
        modelMapper.createTypeMap(CreteTournamentRequest.class, TournamentDTO.class)
                .setConverter(new CreateTournamentRequestToTournamentDtoConverter());
        modelMapper.createTypeMap(UpdateTournamentRequest.class, PatchTournament.class)
                .setConverter(new UpdateTournamentRequestToPatchTournamentConverter());
        modelMapper.createTypeMap(EnrollTournamentParticipantRequest.class, List.class)
                .setConverter(new EnrollTournamentParticipantRequestToResourceObjectListConverter());
        modelMapper.createTypeMap(UpdateContestScoreRequest.class, ScoreDTO.class)
                .setConverter(new UpdateContestScoreRequestToScoreDtoConverter());

        modelMapper.createTypeMap(SingleContest.class, SingleContestDTO.class)
                .setConverter(new SingleContestToSingleContestDtoConverter());
        modelMapper.createTypeMap(DoubleContest.class, DoubleContestDTO.class)
                .setConverter(new DoubleContestToDoubleContestDtoConverter());
        modelMapper.createTypeMap(EliminationContest.class, EliminationContestDTO.class)
                .setConverter(new EliminationContestToEliminationContestDtoConverter());
    }
}
