package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.RequestParameterHelper;
import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.request.CreateTournament;
import com.dss.tennis.tournament.tables.model.response.v1.*;
import com.dss.tennis.tournament.tables.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        SuccessResponseDTO<TournamentDTO> tournamentDTO = tournamentService.createNewTournament(tournamentDto);
        SuccessResponse<GetTournament> tournamentResponse = responseHelper
                .createSuccessResponse(tournamentDTO, GetTournament.class);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<SuccessResponse<GetTournament>> getTournamentById(@PathVariable Integer tournamentId,
                                                                            @RequestParam(required = false) String include) {
        RequestParameter requestParameters = new RequestParameter();
        List<ErrorData> warnings = requestParameterHelper
                .populateRequestParameter(RequestParameterHelper.INCLUDE_KEY, include, requestParameters);
        TournamentDTO tournamentDTO = tournamentService.getTournament(tournamentId, requestParameters);

        SuccessResponse<GetTournament> tournamentResponse = responseHelper
                .createSuccessResponse(tournamentDTO, warnings);
        return new ResponseEntity<>(tournamentResponse, HttpStatus.OK);
    }
}
