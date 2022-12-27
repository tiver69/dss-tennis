package ua.com.dss.tennis.tournament.api.converter;

import org.modelmapper.ModelMapper;
import ua.com.dss.tennis.tournament.api.converter.dto.DoubleContestToDoubleContestDtoConverter;
import ua.com.dss.tennis.tournament.api.converter.dto.EliminationContestToEliminationContestDtoConverter;
import ua.com.dss.tennis.tournament.api.converter.dto.SingleContestToSingleContestDtoConverter;
import ua.com.dss.tennis.tournament.api.converter.patch.PlayerDtoPathApplier;
import ua.com.dss.tennis.tournament.api.converter.patch.TournamentDtoPathApplier;
import ua.com.dss.tennis.tournament.api.converter.request.*;
import ua.com.dss.tennis.tournament.api.converter.response.*;
import ua.com.dss.tennis.tournament.api.converter.response.pageable.PageableDtoToPageablePlayerResponse;
import ua.com.dss.tennis.tournament.api.converter.response.pageable.PageableDtoToPageableTeamResponse;
import ua.com.dss.tennis.tournament.api.converter.response.pageable.PageableDtoToPageableTournamentResponse;
import ua.com.dss.tennis.tournament.api.model.db.v2.DoubleContest;
import ua.com.dss.tennis.tournament.api.model.db.v2.EliminationContest;
import ua.com.dss.tennis.tournament.api.model.db.v2.SingleContest;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestInfoResponse.ContestInfoResponseData;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestInfoResponse.EliminationContestInfoResponseData;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestRequest.UpdateContestScoreRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestResponse.ContestResponseData;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PageablePlayerResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerPatch;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerRequest.CretePlayerRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerRequest.UpdatePlayerRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerResponse.PlayerResponseData;
import ua.com.dss.tennis.tournament.api.model.definitions.team.CreateTeamRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.team.PageableTeamResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.team.TeamResponse.TeamResponseData;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.EnrollTournamentParticipantRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.PageableTournamentResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentPatch;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentRequest.CreteTournamentRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentRequest.UpdateTournamentRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentResponse.TournamentResponseData;
import ua.com.dss.tennis.tournament.api.model.dto.*;

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
        modelMapper.createTypeMap(PlayerPatch.class, PlayerDTO.class)
                .setConverter(new PlayerDtoPathApplier());
        modelMapper.createTypeMap(TournamentPatch.class, TournamentDTO.class)
                .setConverter(new TournamentDtoPathApplier());
        return modelMapper;
    }

    private static void applyConvertersForV2(ModelMapper modelMapper) {
        modelMapper.createTypeMap(PlayerDTO.class, PlayerResponseData.class)
                .setConverter(new PlayerDtoToPlayerResponseDataConverter());
        modelMapper.createTypeMap(UpdatePlayerRequest.class, PlayerPatch.class)
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
        modelMapper.createTypeMap(UpdateTournamentRequest.class, TournamentPatch.class)
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
