package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.RequestParameterHelper;
import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.request.CreateScore;
import com.dss.tennis.tournament.tables.model.request.CreateTournament;
import com.dss.tennis.tournament.tables.model.request.EnrollTournamentParticipant;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetPageable;
import com.dss.tennis.tournament.tables.model.response.v1.GetTournament;
import com.dss.tennis.tournament.tables.model.response.v1.SuccessResponse;
import com.dss.tennis.tournament.tables.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.dss.tennis.tournament.tables.validator.PageableValidator.PAGE_DEFAULT_STRING;
import static com.dss.tennis.tournament.tables.validator.PageableValidator.PAGE_SIZE_DEFAULT_STRING;

@RestController
@RequestMapping("/tournaments")
@CrossOrigin
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private ResponseHelper responseHelper;
    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private RequestParameterHelper requestParameterHelper;

    @PostMapping
    public ResponseEntity<SuccessResponse<GetTournament>> createTournament(@RequestBody CreateTournament tournament) {
        TournamentDTO tournamentDto = converterHelper.convert(tournament, TournamentDTO.class, true);

        TournamentDTO createdTournamentDto = tournamentService.createNewTournament(tournamentDto);
        SuccessResponse<GetTournament> tournamentResponse = responseHelper
                .createSuccessResponse(createdTournamentDto, GetTournament.class);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{tournamentId}/participants")
    public ResponseEntity<SuccessResponse<GetTournament>> enrollTournamentParticipant(
            @PathVariable Integer tournamentId,
            @RequestBody EnrollTournamentParticipant participants) {
        List<ResourceObjectDTO> participantsDto = converterHelper
                .convert(participants.getData(), ResourceObjectDTO.class, true);

        SuccessResponseDTO<TournamentDTO> tournamentDTO = tournamentService
                .addParticipantsToTournament(tournamentId, participantsDto);

        SuccessResponse<GetTournament> tournamentResponse = responseHelper
                .createSuccessResponse(tournamentDTO, GetTournament.class);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{tournamentId}/contest/{contestId}/score")
    public ResponseEntity<SuccessResponse<?>> createScore(@PathVariable Integer tournamentId,
                                                          @PathVariable Integer contestId,
                                                          @RequestBody CreateScore score) {
        ScoreDTO scoreDto = converterHelper.convert(score, ScoreDTO.class);

        ContestDTO contestDto = tournamentService.createContestScore(contestId, tournamentId, scoreDto);
        SuccessResponse<GetContest> contestResponse = responseHelper
                .createSuccessResponse(contestDto, GetContest.class);
        return new ResponseEntity<>(contestResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<SuccessResponse<GetTournament>> getTournamentById(@PathVariable Integer tournamentId,
                                                                            @RequestParam(required = false) String include) {
        RequestParameter requestParameters = new RequestParameter();
        Set<ErrorDataDTO> warnings = requestParameterHelper
                .populateRequestParameter(RequestParameterHelper.INCLUDE_KEY, include, requestParameters);
        TournamentDTO tournamentDTO = tournamentService.getTournament(tournamentId, requestParameters);

        SuccessResponse<GetTournament> tournamentResponse = responseHelper
                .createSuccessResponse(tournamentDTO, warnings);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<GetPageable>> getPageableTournaments(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
        SuccessResponseDTO<PageableDTO<TournamentDTO>> pageableTournamentsDto = tournamentService
                .getTournamentPage(page, pageSize);

        SuccessResponse<GetPageable> tournamentsSuccessResponse = responseHelper
                .createSuccessResponse(pageableTournamentsDto, GetPageable.class);
        return new ResponseEntity<>(tournamentsSuccessResponse, HttpStatus.OK);
    }


}
