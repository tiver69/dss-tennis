package com.dss.tennis.tournament.tables.controller.v2;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.player.PageablePlayerResponse;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.CretePlayerRequest;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.PlayerRequestAttributes;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerRequest.UpdatePlayerRequest;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseAttributes;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.PLAYER;
import static com.dss.tennis.tournament.tables.validator.PageableValidator.PAGE_DEFAULT_STRING;
import static com.dss.tennis.tournament.tables.validator.PageableValidator.PAGE_SIZE_DEFAULT_STRING;

@RestController
@RequestMapping("/participants")
@CrossOrigin
public class ParticipantPlayerController {

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/players")
    public ResponseEntity<PageablePlayerResponse> getPageablePlayers(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT_STRING) int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT_STRING) byte pageSize) {
        SuccessResponseDTO<PageableDTO> pageablePlayersDto = participantService
                .getParticipantPage(page, pageSize, PLAYER);

        //todo: plus warnings
        PageablePlayerResponse playersSuccessResponse =
                tempMapPageablePlayerResponse(pageablePlayersDto.getData(), pageSize);
        return new ResponseEntity<>(playersSuccessResponse, HttpStatus.OK);
    }

    @PostMapping("/players")
    public ResponseEntity<PlayerResponse> createPlayer(@RequestBody CretePlayerRequest createPlayer) {
        PlayerDTO playerDto = tempMapPlayerCreateRequestData(createPlayer);

        PlayerDTO newPlayerDto = participantService.createNewPlayer(playerDto);
        PlayerResponse playerResponse = new PlayerResponse(tempMapPlayerResponseData(newPlayerDto));
        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/players/{playerId}")
    public ResponseEntity<PlayerResponse> updatePlayer(@RequestBody UpdatePlayerRequest updatePlayer,
                                          @PathVariable Integer playerId) {
        //todo: validate ids form request and model
        PatchPlayer patch = tempMapPlayerUpdateRequestData(updatePlayer);

        PlayerDTO updatedPlayer = participantService.updatePlayer(patch, playerId);
        PlayerResponse playerResponse = new PlayerResponse(tempMapPlayerResponseData(updatedPlayer));
        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/players/{playerId}")
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable Integer playerId) {
        PlayerDTO player = participantService.getPlayerDTO(playerId);

        PlayerResponse playerResponse = new PlayerResponse(tempMapPlayerResponseData(player));
        return new ResponseEntity<>(playerResponse, HttpStatus.OK);
    }

    private PatchPlayer tempMapPlayerUpdateRequestData(UpdatePlayerRequest playerRequest) {
        PlayerRequestAttributes playerAttributes = playerRequest.getAttributes();

        PatchPlayer patchPlayer = new PatchPlayer();
        patchPlayer.setFirstName(Optional.ofNullable(playerAttributes.getFirstName()));
        patchPlayer.setLastName(Optional.ofNullable(playerAttributes.getLastName()));
        patchPlayer.setBirthDate(Optional.ofNullable(playerAttributes.getBirthDate()));
        patchPlayer.setExperienceYear(Optional.ofNullable(playerAttributes.getExperienceYear()));
        patchPlayer.setLeadingHand(Optional.ofNullable(playerAttributes.getLeadingHand()));
        return patchPlayer;
    }

    private PlayerDTO tempMapPlayerCreateRequestData(CretePlayerRequest playerRequest) {
        PlayerRequestAttributes playerAttributes = playerRequest.getAttributes();

        PlayerDTO playerDto = new PlayerDTO();
        playerDto.setFirstName(playerAttributes.getFirstName());
        playerDto.setLastName(playerAttributes.getLastName());
        playerDto.setBirthDate(playerAttributes.getBirthDate());
        playerDto.setExperienceYear(playerAttributes.getExperienceYear());
        playerDto.setLeadingHand(playerAttributes.getLeadingHand());
        return playerDto;
    }

    private PageablePlayerResponse tempMapPageablePlayerResponse(PageableDTO<PlayerDTO> pageablePlayersDto,
                                                                 Byte pageSize) {
        return PageablePlayerResponse.builder()
                .totalPages(pageablePlayersDto.getTotalPages())
                .page(pageablePlayersDto.getPage().stream().map(this::tempMapPlayerResponseData)
                        .collect(Collectors.toList()))
                .links(Links.builder()
                        .first(String.format(PLAYER.pageableLinkFormat, 1, pageSize))
                        .last(String.format(PLAYER.pageableLinkFormat, 100, pageSize)) //todo
                        .prev(String
                                .format(PLAYER.pageableLinkFormat, pageablePlayersDto
                                        .getCurrentPage() - 1, pageSize))
                        .self(String.format(PLAYER.pageableLinkFormat, pageablePlayersDto
                                .getCurrentPage(), pageSize))
                        .next(String
                                .format(PLAYER.pageableLinkFormat, pageablePlayersDto
                                        .getCurrentPage() + 1, pageSize))
                        .build())
                .build();
    }

    private PlayerResponseData tempMapPlayerResponseData(PlayerDTO playerDto) {
        PlayerResponseData responseData = new PlayerResponseData();
        responseData.setAttributes(PlayerResponseAttributes.builder()
                .firstName(playerDto.getFirstName())
                .lastName(playerDto.getLastName())
                .age(countAge(playerDto.getBirthDate()))
                .experience(countExperience(playerDto.getExperienceYear()))
                .leadingHand(playerDto.getLeadingHand())
                .build());
        responseData.setId(playerDto.getId());
        responseData.setLinks(Links.builder()
                .self(String.format(PLAYER.selfLinkFormat, playerDto.getId()))
                .build()
        );
        return responseData;
    }

    private Integer countAge(LocalDate birthDate) {
        return birthDate != null ? Period.between(birthDate, LocalDate.now()).getYears() : null;
    }

    private Integer countExperience(Integer experienceStartYear) {
        return experienceStartYear != null ? LocalDate.now().getYear() - experienceStartYear : null;
    }
}
