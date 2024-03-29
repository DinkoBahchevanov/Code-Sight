package com.codesight.codesight_api.domain.challenge.service;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChallengeService {

    Page<ChallengeGetDto> get(int pageNumber, int pageSize, String sortBy, String direction);

    ChallengeGetDto create(ChallengePostDto challengeRequestDto);

    void delete(int id);

    ChallengeGetDto update(int id, ChallengePostDto challengePostDto);

    ChallengeGetDto getById(int id);

    ChallengeGetDto partialUpdate(int id, JsonPatch patch);
}
