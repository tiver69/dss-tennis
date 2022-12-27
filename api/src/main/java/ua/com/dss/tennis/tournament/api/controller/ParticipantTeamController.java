package ua.com.dss.tennis.tournament.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.helper.ResponseHelper;
import ua.com.dss.tennis.tournament.api.model.definitions.Pageable.PageableResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.team.CreateTeamRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.team.PageableTeamResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.team.TeamResponse;
import ua.com.dss.tennis.tournament.api.model.dto.PageableDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ResponseWarningDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TeamDTO;
import ua.com.dss.tennis.tournament.api.service.ParticipantService;
import ua.com.dss.tennis.tournament.api.validator.aop.anotation.TeamCreationBeforeValidator;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TEAM;
import static ua.com.dss.tennis.tournament.api.validator.PageableValidator.PAGE_DEFAULT_STRING;
import static ua.com.dss.tennis.tournament.api.validator.PageableValidator.PAGE_SIZE_DEFAULT_STRING;

@RestController
@RequestMapping("/participants")
@CrossOrigin
public class ParticipantTeamController {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private ResponseHelper responseHelper;
    @Autowired
    private ParticipantService participantService;

    @GetMapping("/teams/{teamId}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Integer teamId) {
        TeamDTO teamDto = participantService.getTeamDTO(teamId);

        TeamResponse teamResponseData = responseHelper.createTeamResponse(teamDto);
        return new ResponseEntity<>(teamResponseData, HttpStatus.OK);
    }

    @GetMapping("/teams")
    public ResponseEntity<PageableResponse> getPageableTeams(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
        ResponseWarningDTO<PageableDTO> pageableTeamsDto = participantService
                .getParticipantPage(page - 1, pageSize, TEAM);

        PageableResponse teamsResponse = converterHelper.convertPageable(pageableTeamsDto, PageableTeamResponse.class);
        return new ResponseEntity<>(teamsResponse, HttpStatus.OK);
    }

    @PostMapping("/teams")
    @TeamCreationBeforeValidator
    public ResponseEntity<?> createTeam(@RequestBody CreateTeamRequest createTeam) {
        TeamDTO teamDto = converterHelper.convert(createTeam, TeamDTO.class);

        TeamDTO newTeamDto = participantService.createNewTeam(teamDto);

        TeamResponse teamResponseData = responseHelper.createTeamResponse(newTeamDto);
        return new ResponseEntity<>(teamResponseData, HttpStatus.CREATED);
    }

}
