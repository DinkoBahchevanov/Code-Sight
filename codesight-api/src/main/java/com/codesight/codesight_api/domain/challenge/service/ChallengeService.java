package com.codesight.codesight_api.domain.challenge.service;

import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public interface ChallengeService {

    ArrayList<ChallengeGetDto> getChallenges();

    ChallengeGetDto create(ChallengePostDto challengeRequestDto);

    ResponseEntity<HttpStatus> delete(int id);
}
