package com.codesight.codesight_api.web.controllers;

import com.codesight.codesight_api.domain.challenge.service.ChallengeService;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePartialDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping
    @ResponseBody
    ResponseEntity<ArrayList<ChallengeGetDto>> get() {
        return new ResponseEntity(challengeService.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    ChallengeGetDto getById(@PathVariable int id) {
        return challengeService.getById(id);
    }

    @PostMapping
    @ResponseBody
    ResponseEntity<HttpStatus> create(@RequestBody @Valid ChallengePostDto challengePostDto) {
        return new ResponseEntity(challengeService.create(challengePostDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> delete(@PathVariable int id) {
       challengeService.delete(id);
       return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @ResponseBody
    ResponseEntity<ChallengeGetDto> update(@PathVariable int id, @RequestBody @Valid ChallengePostDto challengePostDto) {
        return new ResponseEntity(challengeService.update(id, challengePostDto), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    @Operation(description = "Updates an existing user", summary = "Updates an existing user")
    @ApiResponses(value = { @ApiResponse(responseCode = "202", description = "The user has been updated", content = @Content)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<ChallengeGetDto> partialUpdate(@PathVariable int id, @RequestBody JsonPatch patch) {
        return new ResponseEntity(challengeService.partialUpdate(id, patch), HttpStatus.OK);
    }
}
