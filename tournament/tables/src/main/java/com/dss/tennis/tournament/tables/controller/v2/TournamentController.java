package com.dss.tennis.tournament.tables.controller.v2;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.RequestParameterHelper;
import com.dss.tennis.tournament.tables.model.definitions.tournament.EnrollTournamentParticipantRequest;
import com.dss.tennis.tournament.tables.model.definitions.tournament.PageableTournamentResponse;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentRequest.CreteTournamentRequest;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentRequest.UpdateTournamentRequest;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseData;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.request.PatchTournament;
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
    private ConverterHelper converterHelper;
    @Autowired
    private RequestParameterHelper requestParameterHelper;

    @GetMapping("/{tournamentId}")
    public ResponseEntity<TournamentResponse> getTournamentById(@PathVariable Integer tournamentId,
                                                                @RequestParam(required = false) String include) {
        //todo: deal with query param
        RequestParameter requestParameters = new RequestParameter();
        Set<ErrorDataDTO> warnings = requestParameterHelper
                .populateRequestParameter(RequestParameterHelper.INCLUDE_KEY, include, requestParameters);
        TournamentDTO tournamentDTO = tournamentService.getTournament(tournamentId, requestParameters);

        TournamentResponseData tournamentResponseData = converterHelper
                .convert(tournamentDTO, TournamentResponseData.class);
        return new ResponseEntity<>(new TournamentResponse(tournamentResponseData), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<PageableTournamentResponse> getPageableTournaments(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
        SuccessResponseDTO<PageableDTO<TournamentDTO>> pageableTournamentsDto = tournamentService
                .getTournamentPage(page, pageSize);

        PageableTournamentResponse tournamentsSuccessResponse = converterHelper
                .convert(pageableTournamentsDto.getData(), PageableTournamentResponse.class);
        return new ResponseEntity<>(tournamentsSuccessResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(@RequestBody CreteTournamentRequest tournament) {
        TournamentDTO tournamentDto = converterHelper.convert(tournament, TournamentDTO.class);

        TournamentDTO createdTournamentDto = tournamentService.createNewTournament(tournamentDto);
        TournamentResponseData tournamentResponseData = converterHelper
                .convert(createdTournamentDto, TournamentResponseData.class);
        return new ResponseEntity<>(new TournamentResponse(tournamentResponseData), HttpStatus.CREATED);
    }

    @PatchMapping("/{tournamentId}")
    public ResponseEntity<TournamentResponse> updateTournament(@RequestBody UpdateTournamentRequest updateTournamentRequest,
                                                               @PathVariable Integer tournamentId) {
        //todo: validate ids form request and model
        PatchTournament patch = converterHelper.convert(updateTournamentRequest, PatchTournament.class);

        TournamentDTO createdTournamentDto = tournamentService.updateTournament(patch, tournamentId);

        TournamentResponseData tournamentResponseData = converterHelper
                .convert(createdTournamentDto, TournamentResponseData.class);
        return new ResponseEntity<>(new TournamentResponse(tournamentResponseData), HttpStatus.CREATED);
    }

    @DeleteMapping("/{tournamentId}")
    public ResponseEntity removeTournament(@PathVariable Integer tournamentId) {
        tournamentService.removeTournament(tournamentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{tournamentId}/enroll")
    public ResponseEntity<TournamentResponse> enrollTournamentParticipant(
            @PathVariable Integer tournamentId,
            @RequestBody EnrollTournamentParticipantRequest participants) {
        //todo: validate ids form request and model
        List<ResourceObjectDTO> participantsDto = converterHelper.convert(participants, List.class);

        SuccessResponseDTO<TournamentDTO> tournamentDTO = tournamentService
                .addParticipantsToTournamentWithScore(tournamentId, participantsDto);

        //todo: plus warnings
        TournamentResponseData tournamentResponseData = converterHelper
                .convert(tournamentDTO.getData(), TournamentResponseData.class);
        return new ResponseEntity<>(new TournamentResponse(tournamentResponseData), HttpStatus.CREATED);
    }


    @DeleteMapping("/{tournamentId}/withdraw/{participantId}")
    public ResponseEntity<TournamentResponse> removeParticipantFromTournament(
            @PathVariable Integer tournamentId, @PathVariable Integer participantId,
            @RequestParam(required = false, defaultValue = "true") boolean techDefeat) {
        TournamentDTO tournamentDTO = tournamentService
                .removeParticipantFromTournament(participantId, tournamentId, techDefeat);

        TournamentResponseData tournamentResponseData = converterHelper
                .convert(tournamentDTO, TournamentResponseData.class);
        return new ResponseEntity<>(new TournamentResponse(tournamentResponseData), HttpStatus.OK);
    }
}
