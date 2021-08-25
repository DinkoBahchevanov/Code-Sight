package com.codesight.codesight_api.domain.challenge.service;

import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePartialDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;

public interface ChallengeService {

    ArrayList<ChallengeGetDto> get();

    ChallengeGetDto create(ChallengePostDto challengeRequestDto);

    void delete(int id);

    ChallengeGetDto update(int id, ChallengePostDto challengePostDto);

    ChallengeGetDto getById(int id);

    ChallengeGetDto partialUpdate(int id, JsonPatch patch);
}
