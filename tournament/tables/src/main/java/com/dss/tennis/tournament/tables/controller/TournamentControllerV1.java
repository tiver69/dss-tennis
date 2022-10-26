package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.RequestParameterHelper;
import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScorePatchDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import com.dss.tennis.tournament.tables.model.request.CreateScore;
import com.dss.tennis.tournament.tables.model.request.PatchTechDefeat;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.SuccessResponse;
import com.dss.tennis.tournament.tables.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/tournaments")
@CrossOrigin
public class TournamentControllerV1 {

    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private ResponseHelper responseHelper;
    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private RequestParameterHelper requestParameterHelper;

//    @PostMapping
//    public ResponseEntity<SuccessResponse<GetTournament>> createTournament(@RequestBody CreateTournament tournament) {
//        TournamentDTO tournamentDto = converterHelper.convert(tournament, TournamentDTO.class, true);
//
//        TournamentDTO createdTournamentDto = tournamentService.createNewTournament(tournamentDto);
//        SuccessResponse<GetTournament> tournamentResponse = responseHelper
//                .createSuccessResponse(createdTournamentDto, GetTournament.class);
//        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
//    }
//
//    @PatchMapping("/{tournamentId}")
//    public ResponseEntity<SuccessResponse<GetTournament>> updateTournament(@RequestBody PatchTournament patch,
//                                                                           @PathVariable Integer tournamentId) {
//        TournamentDTO createdTournamentDto = tournamentService.updateTournament(patch, tournamentId);
//        SuccessResponse<GetTournament> tournamentResponse = responseHelper
//                .createSuccessResponse(createdTournamentDto, GetTournament.class);
//        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
//    }

//    @PostMapping("/{tournamentId}/participants")
//    public ResponseEntity<SuccessResponse<GetTournament>> enrollTournamentParticipant(
//            @PathVariable Integer tournamentId,
//            @RequestBody EnrollTournamentParticipant participants) {
//        List<ResourceObjectDTO> participantsDto = converterHelper
//                .convert(participants.getData(), ResourceObjectDTO.class, true);
//
//        SuccessResponseDTO<TournamentDTO> tournamentDTO = tournamentService
//                .addParticipantsToTournament(tournamentId, participantsDto);
//
//        SuccessResponse<GetTournament> tournamentResponse = responseHelper
//                .createSuccessResponse(tournamentDTO, GetTournament.class);
//        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
//    }

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

    @PatchMapping("/{tournamentId}/contest/{contestId}/score")
    public ResponseEntity<SuccessResponse<?>> updateScore(@PathVariable Integer tournamentId,
                                                          @PathVariable Integer contestId,
                                                          @RequestBody CreateScore score) {
        ScorePatchDTO scorePatchDto = converterHelper.convert(score, ScorePatchDTO.class);

        ContestDTO contestDto = tournamentService.updateContestScore(contestId, tournamentId, scorePatchDto);
        SuccessResponse<GetContest> contestResponse = responseHelper
                .createSuccessResponse(contestDto, GetContest.class);
        return new ResponseEntity<>(contestResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{tournamentId}/contest/{contestId}/techDefeat")
    public ResponseEntity<SuccessResponse<?>> updateTechDefeat(@PathVariable Integer tournamentId,
                                                               @PathVariable Integer contestId,
                                                               @RequestBody PatchTechDefeat techDefeat) {
        TechDefeatDTO techDefeatDto = converterHelper.convert(techDefeat, TechDefeatDTO.class);

        ContestDTO contestDto = tournamentService.updateContestTechDefeat(contestId, tournamentId, techDefeatDto);
        SuccessResponse<GetContest> contestResponse = responseHelper
                .createSuccessResponse(contestDto, GetContest.class);
        return new ResponseEntity<>(contestResponse, HttpStatus.CREATED);
    }

//    @GetMapping("/{tournamentId}")
//    public ResponseEntity<SuccessResponse<GetTournament>> getTournamentById(@PathVariable Integer tournamentId,
//                                                                            @RequestParam(required = false) String include) {
//        RequestParameter requestParameters = new RequestParameter();
//        Set<ErrorDataDTO> warnings = requestParameterHelper
//                .populateRequestParameter(RequestParameterHelper.INCLUDE_KEY, include, requestParameters);
//        TournamentDTO tournamentDTO = tournamentService.getTournament(tournamentId, requestParameters);
//
//        SuccessResponse<GetTournament> tournamentResponse = responseHelper
//                .createSuccessResponse(tournamentDTO, warnings);
//        return new ResponseEntity<>(tournamentResponse, HttpStatus.OK);
//    }
//
//    @GetMapping
//    public ResponseEntity<SuccessResponse<GetPageable>> getPageableTournaments(
//            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
//            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
//        SuccessResponseDTO<PageableDTO<TournamentDTO>> pageableTournamentsDto = tournamentService
//                .getTournamentPage(page, pageSize);
//
//        SuccessResponse<GetPageable> tournamentsSuccessResponse = responseHelper
//                .createSuccessResponse(pageableTournamentsDto, GetPageable.class);
//        return new ResponseEntity<>(tournamentsSuccessResponse, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{tournamentId}/participants/{participantId}")
//    public ResponseEntity<SuccessResponse<GetTournament>> removeParticipantFromTournament(
//            @PathVariable Integer tournamentId, @PathVariable Integer participantId,
//            @RequestParam(required = false, defaultValue = "true") boolean techDefeat) {
//        TournamentDTO tournamentDTO = tournamentService
//                .removeParticipantFromTournament(participantId, tournamentId, techDefeat);
//
//        SuccessResponse<GetTournament> tournamentResponse = responseHelper
//                .createSuccessResponse(tournamentDTO, GetTournament.class);
//        return new ResponseEntity<>(tournamentResponse, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{tournamentId}")
//    public ResponseEntity removeTournament(@PathVariable Integer tournamentId) {
//        tournamentService.removeTournament(tournamentId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
