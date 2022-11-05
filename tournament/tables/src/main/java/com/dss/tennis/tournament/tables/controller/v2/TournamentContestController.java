package com.dss.tennis.tournament.tables.controller.v2;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestRequest.UpdateContestScoreRequest;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ContestScorePatchDTO;
import com.dss.tennis.tournament.tables.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tournaments")
@CrossOrigin
public class TournamentContestController {

    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private ResponseHelper responseHelper;

    @GetMapping("/{tournamentId}/contest/{contestId}")
    public ResponseEntity<ContestResponse> getTournamentContest(@PathVariable Integer tournamentId,
                                                                @PathVariable Integer contestId) {
        ContestDTO contestDto = tournamentService.getTournamentContest(contestId, tournamentId);

        ContestResponse contestResponse = responseHelper.createContestResponse(contestDto, tournamentId);
        return new ResponseEntity<>(contestResponse, HttpStatus.OK);
    }

    @PatchMapping("/{tournamentId}/contest/{contestId}/score")
    public ResponseEntity<ContestResponse> updateScore(@PathVariable Integer tournamentId,
                                                       @PathVariable Integer contestId,
                                                       @RequestBody UpdateContestScoreRequest updateContestScoreRequest) {
        //todo: validate id from body and url
        ContestScorePatchDTO scorePatchDto = converterHelper
                .convert(updateContestScoreRequest, ContestScorePatchDTO.class);

        ContestDTO contestDto = tournamentService.updateContestScore(contestId, tournamentId, scorePatchDto);

        ContestResponse contestResponse = responseHelper.createContestResponse(contestDto, tournamentId);
        return new ResponseEntity<>(contestResponse, HttpStatus.OK);
    }
}
