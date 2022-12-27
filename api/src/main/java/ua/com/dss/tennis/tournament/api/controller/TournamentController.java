package ua.com.dss.tennis.tournament.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.helper.RequestParameterHelper;
import ua.com.dss.tennis.tournament.api.helper.ResponseHelper;
import ua.com.dss.tennis.tournament.api.model.definitions.Pageable.PageableResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.EnrollTournamentParticipantRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.PageableTournamentResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentPatch;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentRequest.CreteTournamentRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentRequest.UpdateTournamentRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentResponse;
import ua.com.dss.tennis.tournament.api.model.dto.PageableDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ResourceObjectDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ResponseWarningDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;
import ua.com.dss.tennis.tournament.api.service.TournamentService;
import ua.com.dss.tennis.tournament.api.validator.aop.anotation.PatchIdValidator;

import java.util.List;

import static ua.com.dss.tennis.tournament.api.validator.PageableValidator.PAGE_DEFAULT_STRING;
import static ua.com.dss.tennis.tournament.api.validator.PageableValidator.PAGE_SIZE_DEFAULT_STRING;

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

        PageableResponse tournamentsResponse = converterHelper
                .convertPageable(pageableTournamentsDto, PageableTournamentResponse.class);
        return new ResponseEntity<>(tournamentsResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(@RequestBody CreteTournamentRequest tournament) {
        TournamentDTO tournamentDto = converterHelper.convert(tournament, TournamentDTO.class);

        TournamentDTO createdTournamentDto = tournamentService.createNewTournament(tournamentDto);

        TournamentResponse tournamentResponse = responseHelper.createTournamentResponse(createdTournamentDto);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{tournamentId}")
    @PatchIdValidator(pathIdParameterName = "tournamentId")
    public ResponseEntity<TournamentResponse> updateTournament(@RequestBody UpdateTournamentRequest updateTournamentRequest,
                                                               @PathVariable Integer tournamentId) {
        TournamentPatch patch = converterHelper.convert(updateTournamentRequest, TournamentPatch.class);

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
    @PatchIdValidator(pathIdParameterName = "tournamentId")
    public ResponseEntity<TournamentResponse> enrollTournamentParticipant(
            @PathVariable Integer tournamentId,
            @RequestBody EnrollTournamentParticipantRequest participants) {
        List<ResourceObjectDTO> participantsDto = converterHelper.convert(participants, List.class);

        ResponseWarningDTO<TournamentDTO> tournamentDTO = tournamentService
                .addParticipantsToTournament(tournamentId, participantsDto);

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
