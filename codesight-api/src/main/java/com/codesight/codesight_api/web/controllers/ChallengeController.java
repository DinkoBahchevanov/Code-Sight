package com.codesight.codesight_api.web.controllers;

import com.codesight.codesight_api.domain.challenge.service.ChallengeService;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping
    ArrayList<ChallengeGetDto> getChallenges() {
        return challengeService.getChallenges();
    }

    @PostMapping
    @ResponseBody
    ChallengeGetDto create(@RequestBody @Valid ChallengePostDto challengePostDto) {
        return challengeService.create(challengePostDto);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> delete(@PathVariable int id) {
       return challengeService.delete(id);
    }
}
