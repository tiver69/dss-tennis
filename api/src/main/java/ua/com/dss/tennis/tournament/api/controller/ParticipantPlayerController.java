package ua.com.dss.tennis.tournament.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.helper.ResponseHelper;
import ua.com.dss.tennis.tournament.api.model.definitions.Pageable.PageableResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PageablePlayerResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerPatch;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerRequest.CretePlayerRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerRequest.UpdatePlayerRequest;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PlayerResponse.PlayerResponseData;
import ua.com.dss.tennis.tournament.api.model.dto.PageableDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ResponseWarningDTO;
import ua.com.dss.tennis.tournament.api.service.ParticipantService;
import ua.com.dss.tennis.tournament.api.validator.aop.anotation.PatchIdValidator;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.PLAYER;
import static ua.com.dss.tennis.tournament.api.validator.PageableValidator.PAGE_DEFAULT_STRING;
import static ua.com.dss.tennis.tournament.api.validator.PageableValidator.PAGE_SIZE_DEFAULT_STRING;

@RestController
@RequestMapping("/participants")
@CrossOrigin
public class ParticipantPlayerController {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private ResponseHelper responseHelper;
    @Autowired
    private ParticipantService participantService;

    @GetMapping("/players")
    public ResponseEntity<PageableResponse> getPageablePlayers(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
        ResponseWarningDTO<PageableDTO> pageablePlayersDto = participantService
                .getParticipantPage(page - 1, pageSize, PLAYER);

        PageableResponse playersResponse = converterHelper
                .convertPageable(pageablePlayersDto, PageablePlayerResponse.class);
        return new ResponseEntity<>(playersResponse, HttpStatus.OK);
    }

    @PostMapping("/players")
    public ResponseEntity<PlayerResponse> createPlayer(@RequestBody CretePlayerRequest createPlayer) {
        PlayerDTO playerDto = converterHelper.convert(createPlayer, PlayerDTO.class);

        PlayerDTO newPlayerDto = participantService.createNewPlayer(playerDto);
        PlayerResponse playerResponse = new PlayerResponse(converterHelper
                .convert(newPlayerDto, PlayerResponseData.class));
        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/players/{playerId}")
    @PatchIdValidator(pathIdParameterName = "playerId")
    public ResponseEntity<PlayerResponse> updatePlayer(@RequestBody UpdatePlayerRequest updatePlayer,
                                                       @PathVariable Integer playerId) {
        PlayerPatch patch = converterHelper.convert(updatePlayer, PlayerPatch.class);

        PlayerDTO updatedPlayer = participantService.updatePlayer(patch, playerId);
        PlayerResponse playerResponse = new PlayerResponse(converterHelper
                .convert(updatedPlayer, PlayerResponseData.class));
        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/players/{playerId}")
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable Integer playerId) {
        PlayerDTO player = participantService.getPlayerDTO(playerId);

        PlayerResponse playerResponse = new PlayerResponse(converterHelper.convert(player, PlayerResponseData.class));
        return new ResponseEntity<>(playerResponse, HttpStatus.OK);
    }
}
