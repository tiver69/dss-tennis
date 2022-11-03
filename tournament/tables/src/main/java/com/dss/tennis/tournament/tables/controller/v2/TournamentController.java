package com.dss.tennis.tournament.tables.controller.v2;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.RequestParameterHelper;
import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.model.definitions.PageableResponse;
import com.dss.tennis.tournament.tables.model.definitions.tournament.EnrollTournamentParticipantRequest;
import com.dss.tennis.tournament.tables.model.definitions.tournament.PageableTournamentResponse;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentRequest.CreteTournamentRequest;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentRequest.UpdateTournamentRequest;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.ResourceObjectDTO;
import com.dss.tennis.tournament.tables.model.dto.ResponseWarningDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.request.PatchTournament;
import com.dss.tennis.tournament.tables.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{tournamentId}")
    public ResponseEntity<TournamentResponse> getTournamentById(@PathVariable Integer tournamentId) {
        TournamentDTO tournamentDTO = tournamentService.getTournament(tournamentId);

        TournamentResponse tournamentResponse = responseHelper.createTournamentResponse(tournamentDTO);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse> getPageableTournaments(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
        ResponseWarningDTO<PageableDTO> pageableTournamentsDto = tournamentService
                .getTournamentPage(page - 1, pageSize);

        PageableResponse tournamentsSuccessResponse = responseHelper
                .createPageableResponse(pageableTournamentsDto, PageableTournamentResponse.class);
        return new ResponseEntity<>(tournamentsSuccessResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(@RequestBody CreteTournamentRequest tournament) {
        TournamentDTO tournamentDto = converterHelper.convert(tournament, TournamentDTO.class);

        TournamentDTO createdTournamentDto = tournamentService.createNewTournament(tournamentDto);

        TournamentResponse tournamentResponse = responseHelper.createTournamentResponse(createdTournamentDto);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{tournamentId}")
    public ResponseEntity<TournamentResponse> updateTournament(@RequestBody UpdateTournamentRequest updateTournamentRequest,
                                                               @PathVariable Integer tournamentId) {
        //todo: validate ids form request and model
        PatchTournament patch = converterHelper.convert(updateTournamentRequest, PatchTournament.class);

        TournamentDTO updatedTournament = tournamentService.updateTournament(patch, tournamentId);

        TournamentResponse tournamentResponse = responseHelper.createTournamentResponse(updatedTournament);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
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

        ResponseWarningDTO<TournamentDTO> tournamentDTO = tournamentService
                .addParticipantsToTournamentWithScore(tournamentId, participantsDto);

        TournamentResponse tournamentResponse = responseHelper.createTournamentResponse(tournamentDTO);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
    }


    @DeleteMapping("/{tournamentId}/withdraw/{participantId}")
    public ResponseEntity<TournamentResponse> removeParticipantFromTournament(
            @PathVariable Integer tournamentId, @PathVariable Integer participantId,
            @RequestParam(required = false, defaultValue = "true") boolean techDefeat) {
        TournamentDTO tournamentDTO = tournamentService
                .removeParticipantFromTournament(participantId, tournamentId, techDefeat);

        TournamentResponse tournamentResponse = responseHelper.createTournamentResponse(tournamentDTO);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.OK);
    }
}
