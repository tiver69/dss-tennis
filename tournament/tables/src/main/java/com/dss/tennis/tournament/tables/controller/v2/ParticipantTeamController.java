package com.dss.tennis.tournament.tables.controller.v2;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.model.definitions.team.CreateTeamRequest;
import com.dss.tennis.tournament.tables.model.definitions.team.PageableTeamResponse;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseData;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.SuccessResponseDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import com.dss.tennis.tournament.tables.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dss.tennis.tournament.tables.validator.PageableValidator.PAGE_DEFAULT_STRING;
import static com.dss.tennis.tournament.tables.validator.PageableValidator.PAGE_SIZE_DEFAULT_STRING;

@RestController
@RequestMapping("/participants")
@CrossOrigin
public class ParticipantTeamController {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private ParticipantService participantService;

    @GetMapping("/teams/{teamId}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Integer teamId) {
        TeamDTO teamDto = participantService.getTeamDTO(teamId);

        TeamResponseData teamResponseData = converterHelper.convert(teamDto, TeamResponseData.class);
        return new ResponseEntity<>(new TeamResponse(teamResponseData), HttpStatus.OK);
    }

    @GetMapping("/teams")
    public ResponseEntity<PageableTeamResponse> getPageableTeams(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
        SuccessResponseDTO<PageableDTO> pageableTeamsDto = participantService
                .getParticipantPage1(page, pageSize, ResourceObjectType.TEAM);

        //todo: plus warnings
        PageableTeamResponse teamsSuccessResponse = converterHelper
                .convert(pageableTeamsDto.getData(), PageableTeamResponse.class);
        return new ResponseEntity<>(teamsSuccessResponse, HttpStatus.OK);
    }

    @PostMapping("/teams")
    public ResponseEntity<?> createTeam(@RequestBody CreateTeamRequest createTeam) {
        TeamDTO teamDto = converterHelper.convert(createTeam, TeamDTO.class);

        TeamDTO newTeamDto = participantService.createNewTeam(teamDto);

        TeamResponseData teamResponseData = converterHelper.convert(newTeamDto, TeamResponseData.class);
        return new ResponseEntity<>(new TeamResponse(teamResponseData), HttpStatus.OK);
    }

}
