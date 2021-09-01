package com.codesight.codesight_api.web.controllers;

import com.codesight.codesight_api.domain.challenge.service.ChallengeService;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/challenges")
@Api(value = "Challenge-Controller")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @ApiOperation(value = "Get list of Challenges")
    @ApiResponse(code = 200, message = "Success|OK")
    @GetMapping(produces = "application/json")
    ResponseEntity<Page<ChallengeGetDto>> get(Pageable pageable) {
        return ResponseEntity.ok(challengeService.get(pageable));
    }

    @ApiOperation(value = "Get a Challenge by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Challenge found|OK"),
            @ApiResponse(code = 404, message = "Challenge NOT found|NOT_FOUND")})
    @GetMapping(value = "/{id}", produces = "application/json")
    ChallengeGetDto getById(@PathVariable int id) {
        return challengeService.getById(id);
    }

    @ApiOperation(value = "Create a Challenge")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Challenge created|CREATED"),
            @ApiResponse(code = 400, message = "Bad request|BAD_REQUEST")})
    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ChallengeGetDto> create(@RequestBody @Valid ChallengePostDto challengePostDto) {
        return new ResponseEntity<>(challengeService.create(challengePostDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete a Challenge by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Challenge DELETED|NO_CONTENT"),
            @ApiResponse(code = 404, message = "Challenge NOT found|NOT_FOUND")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> delete(@PathVariable int id) {
        challengeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Partial Update a Challenge by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Challenge UPDATED|OK"),
            @ApiResponse(code = 400, message = "Bad request|BAD_REQUEST"),
            @ApiResponse(code = 404, message = "Challenge NOT found|NOT_FOUND")
    })
    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json", produces = "application/json")
    ResponseEntity<ChallengeGetDto> partialUpdate(@PathVariable int id, @RequestBody JsonMergePatch patch) {
        return ResponseEntity.ok(challengeService.partialUpdate(id, patch));
    }
}
