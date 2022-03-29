package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.SuccessResponseDTO;
import com.dss.tennis.tournament.tables.model.request.CreatePlayer;
import com.dss.tennis.tournament.tables.model.request.PatchPlayer;
import com.dss.tennis.tournament.tables.model.response.v1.GetPageable;
import com.dss.tennis.tournament.tables.model.response.v1.GetPlayer;
import com.dss.tennis.tournament.tables.model.response.v1.SuccessResponse;
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
public class ParticipantController {

    @Autowired
    private ResponseHelper responseHelper;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ConverterHelper converterHelper;

    @PostMapping("/players")
    public ResponseEntity<?> createPlayer(@RequestBody CreatePlayer createPlayer) {
        PlayerDTO playerDto = converterHelper.convert(createPlayer, PlayerDTO.class);

        PlayerDTO newPlayerDto = participantService.createNewPlayer(playerDto);
        SuccessResponse<GetPlayer> playerResponse = responseHelper
                .createSuccessResponse(newPlayerDto, GetPlayer.class);
        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/players/{playerId}")
    public ResponseEntity<?> updatePlayer(@RequestBody PatchPlayer patch, @PathVariable Integer playerId) {
        PlayerDTO updatedPlayer = participantService.updatePlayer(patch, playerId);
        SuccessResponse<GetPlayer> playerResponse = responseHelper
                .createSuccessResponse(updatedPlayer, GetPlayer.class);
        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/players")
    public ResponseEntity<SuccessResponse<GetPageable>> getPageablePlayers(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
        SuccessResponseDTO<PageableDTO<PlayerDTO>> pageablePlayersDto = participantService
                .getPlayersPage(page, pageSize);

        SuccessResponse<GetPageable> playersSuccessResponse = responseHelper
                .createSuccessResponse(pageablePlayersDto, GetPageable.class);
        return new ResponseEntity<>(playersSuccessResponse, HttpStatus.OK);
    }

    @GetMapping("/players/{playerId}")
    public ResponseEntity<SuccessResponse<GetPlayer>> getPlayerById(@PathVariable Integer playerId) {
        PlayerDTO player = participantService.getPlayerDTO(playerId);

        SuccessResponse<GetPlayer> playerResponse = responseHelper.createSuccessResponse(player, GetPlayer.class);
        return new ResponseEntity<>(playerResponse, HttpStatus.OK);
    }
}
