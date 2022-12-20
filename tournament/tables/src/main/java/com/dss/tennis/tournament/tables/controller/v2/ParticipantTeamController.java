package com.dss.tennis.tournament.tables.controller.v2;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.model.definitions.Pageable.PageableResponse;
import com.dss.tennis.tournament.tables.model.definitions.team.CreateTeamRequest;
import com.dss.tennis.tournament.tables.model.definitions.team.PageableTeamResponse;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.ResponseWarningDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import com.dss.tennis.tournament.tables.service.ParticipantService;
import com.dss.tennis.tournament.tables.validator.aop.anotation.TeamCreationBeforeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TEAM;
import static com.dss.tennis.tournament.tables.validator.PageableValidator.PAGE_DEFAULT_STRING;
import static com.dss.tennis.tournament.tables.validator.PageableValidator.PAGE_SIZE_DEFAULT_STRING;

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
