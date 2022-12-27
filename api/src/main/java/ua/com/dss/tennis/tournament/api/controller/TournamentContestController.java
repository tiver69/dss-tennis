package ua.com.dss.tennis.tournament.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.helper.ResponseHelper;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestRequest.UpdateContestScoreRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestResponse;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ScoreDTO;
import ua.com.dss.tennis.tournament.api.service.TournamentService;
import ua.com.dss.tennis.tournament.api.validator.aop.anotation.PatchIdValidator;

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
    @PatchIdValidator(pathIdParameterName = "contestId")
    public ResponseEntity<ContestResponse> updateScore(@PathVariable Integer tournamentId,
                                                       @PathVariable Integer contestId,
                                                       @RequestBody UpdateContestScoreRequest updateContestScoreRequest) {
        ScoreDTO scorePatchDto = converterHelper
                .convert(updateContestScoreRequest, ScoreDTO.class);

        ContestDTO contestDto = tournamentService.updateContestScore(contestId, tournamentId, scorePatchDto);

        ContestResponse contestResponse = responseHelper.createContestResponse(contestDto, tournamentId);
        return new ResponseEntity<>(contestResponse, HttpStatus.OK);
    }
}
