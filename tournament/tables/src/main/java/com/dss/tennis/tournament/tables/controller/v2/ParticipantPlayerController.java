package com.dss.tennis.tournament.tables.controller.v2;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.model.definitions.player.PageablePlayerResponse;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.CretePlayerRequest;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.UpdatePlayerRequest;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.SuccessResponseDTO;
import com.dss.tennis.tournament.tables.model.request.PatchPlayer;
import com.dss.tennis.tournament.tables.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.PLAYER;
import static com.dss.tennis.tournament.tables.validator.PageableValidator.PAGE_DEFAULT_STRING;
import static com.dss.tennis.tournament.tables.validator.PageableValidator.PAGE_SIZE_DEFAULT_STRING;

@RestController
@RequestMapping("/participants")
@CrossOrigin
public class ParticipantPlayerController {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private ParticipantService participantService;

    @GetMapping("/players")
    public ResponseEntity<PageablePlayerResponse> getPageablePlayers(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
        SuccessResponseDTO<PageableDTO> pageablePlayersDto = participantService
                .getParticipantPage(page, pageSize, PLAYER);

        //todo: plus warnings
        PageablePlayerResponse playersSuccessResponse = converterHelper
                .convert(pageablePlayersDto.getData(), PageablePlayerResponse.class);
        return new ResponseEntity<>(playersSuccessResponse, HttpStatus.OK);
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
    public ResponseEntity<PlayerResponse> updatePlayer(@RequestBody UpdatePlayerRequest updatePlayer,
                                                       @PathVariable Integer playerId) {
        //todo: validate ids form request and model
        PatchPlayer patch = converterHelper.convert(updatePlayer, PatchPlayer.class);

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
