package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.model.definitions.Meta.CommonMeta;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.EliminationContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestResponseData;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseData;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseData;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorResponse.ErrorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResponseHelper {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private WarningHandler warningHandler;

    public ContestResponse createContestResponse(ContestDTO contestDTO, Integer tournamentId) {
        ContestResponseData responseData = converterHelper
                .convert(contestDTO, ContestResponseData.class, String.valueOf(tournamentId));

        List<Object> responseIncluded = new ArrayList<>();
        responseIncluded.addAll(convertParticipant(contestDTO.getParticipantOne()));
        responseIncluded.addAll(convertParticipant(contestDTO.getParticipantTwo()));
        return new ContestResponse(responseData, responseIncluded.isEmpty() ? null : responseIncluded);
    }

    private List<Object> convertParticipant(Object participant) {
        if (participant == null) return new ArrayList<>();

        List<Object> participantIncluded = new ArrayList<>();
        if (participant instanceof PlayerDTO) {
            participantIncluded.add(converterHelper.convert(participant, PlayerResponseData.class));
        } else {
            TeamDTO team = (TeamDTO) participant;
            participantIncluded.add(converterHelper.convert(team, TeamResponseData.class));
            participantIncluded.add(converterHelper.convert(team.getPlayerOne(), PlayerResponseData.class));
            participantIncluded.add(converterHelper.convert(team.getPlayerTwo(), PlayerResponseData.class));
        }
        return participantIncluded;
    }

    public TeamResponse createTeamResponse(TeamDTO teamDto) {
        TeamResponseData responseData = converterHelper.convert(teamDto, TeamResponseData.class);

        List<Object> responseIncluded = new ArrayList<>();
        responseIncluded.add(converterHelper.convert(teamDto.getPlayerOne(), PlayerResponseData.class));
        responseIncluded.add(converterHelper.convert(teamDto.getPlayerTwo(), PlayerResponseData.class));

        return new TeamResponse(responseData, responseIncluded);
    }

    public TournamentResponse createTournamentResponse(ResponseWarningDTO<TournamentDTO> responseWarning) {
        TournamentResponse responseData = createTournamentResponse(responseWarning.getData());

        Set<ErrorDataDTO> warningsDto = responseWarning.getWarnings();
        if (warningsDto != null && !warningsDto.isEmpty()) {
            Set<ErrorData> responseWarnings = warningsDto.stream().map(warningHandler::createErrorData)
                    .collect(Collectors.toSet());
            responseData.setMeta(new CommonMeta(responseWarnings));
        }
        return responseData;
    }

    public TournamentResponse createTournamentResponse(TournamentDTO tournamentDto) {
        TournamentResponseData responseData = converterHelper.convert(tournamentDto, TournamentResponseData.class);

        if (tournamentDto.getContests() == null) return new TournamentResponse(null, responseData, null);
        List<Object> responseIncluded = new ArrayList<>();
        tournamentDto.getContests().forEach(contest -> {
            Class<? extends ContestInfoResponseData> responseClass = contest instanceof EliminationContestDTO ?
                    EliminationContestInfoResponseData.class : ContestInfoResponseData.class;
            responseIncluded
                    .add(converterHelper.convert(contest, responseClass, String.valueOf(tournamentDto.getId())));
        });

        return new TournamentResponse(null, responseData, responseIncluded.isEmpty() ? null : responseIncluded);
    }
}
