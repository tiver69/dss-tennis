package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participants")
@CrossOrigin
public class ParticipantController {

    @Autowired
    private ResponseHelper responseHelper;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ConverterHelper converterHelper;

//    @PostMapping("/players")
//    public ResponseEntity<?> createPlayer(@RequestBody CreatePlayer createPlayer) {
//        PlayerDTO playerDto = converterHelper.convert(createPlayer, PlayerDTO.class);
//
//        PlayerDTO newPlayerDto = participantService.createNewPlayer(playerDto);
//        SuccessResponse<GetPlayer> playerResponse = responseHelper.createSuccessResponse(newPlayerDto, GetPlayer.class);
//        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
//    }

//    @PostMapping("/teams")
//    public ResponseEntity<?> createTeam(@RequestBody CreateTeam createTeam) {
//        TeamDTO teamDto = converterHelper.convert(createTeam, TeamDTO.class);
//
//        TeamDTO newTeamDto = participantService.createNewTeam(teamDto);
//        SuccessResponse<GetTeam> teamResponse = responseHelper.createSuccessResponse(newTeamDto, GetTeam.class);
//        return new ResponseEntity<>(teamResponse, HttpStatus.OK);
//    }

//    @PatchMapping("/players/{playerId}")
//    public ResponseEntity<?> updatePlayer(@RequestBody PatchPlayer patch, @PathVariable Integer playerId) {
//        PlayerDTO updatedPlayer = participantService.updatePlayer(patch, playerId);
//        SuccessResponse<GetPlayer> playerResponse = responseHelper
//                .createSuccessResponse(updatedPlayer, GetPlayer.class);
//        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
//    }

//    @GetMapping("/players")
//    public ResponseEntity<SuccessResponse<GetPageable>> getPageablePlayers(
//            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
//            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
//        SuccessResponseDTO<PageableDTO> pageablePlayersDto = participantService
//                .getParticipantPage(page, pageSize, ResourceObjectType.PLAYER);
//
//        SuccessResponse<GetPageable> playersSuccessResponse = responseHelper
//                .createSuccessResponse(pageablePlayersDto, GetPageable.class);
//        return new ResponseEntity<>(playersSuccessResponse, HttpStatus.OK);
//    }
//
//    @GetMapping("/teams")
//    public ResponseEntity<SuccessResponse<GetPageable>> getPageableTeams(
//            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
//            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
//        SuccessResponseDTO<PageableDTO> pageableTeamsDto = participantService
//                .getParticipantPage(page, pageSize, ResourceObjectType.TEAM);
//
//        SuccessResponse<GetPageable> teamsSuccessResponse = responseHelper
//                .createSuccessResponse(pageableTeamsDto, GetPageable.class);
//        return new ResponseEntity<>(teamsSuccessResponse, HttpStatus.OK);
//    }

//    @GetMapping("/players/{playerId}")
//    public ResponseEntity<SuccessResponse<GetPlayer>> getPlayerById(@PathVariable Integer playerId) {
//        PlayerDTO player = participantService.getPlayerDTO(playerId);
//
//        SuccessResponse<GetPlayer> playerResponse = responseHelper.createSuccessResponse(player, GetPlayer.class);
//        return new ResponseEntity<>(playerResponse, HttpStatus.OK);
//    }
//
//    @GetMapping("/teams/{teamId}")
//    public ResponseEntity<SuccessResponse<GetTeam>> getTeamById(@PathVariable Integer teamId) {
//        TeamDTO team = participantService.getTeamDTO(teamId);
//
//        SuccessResponse<GetTeam> teamResponse = responseHelper.createSuccessResponse(team, GetTeam.class);
//        return new ResponseEntity<>(teamResponse, HttpStatus.OK);
//    }
}
