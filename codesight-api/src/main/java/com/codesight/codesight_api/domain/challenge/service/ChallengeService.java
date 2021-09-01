package com.codesight.codesight_api.domain.challenge.service;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChallengeService {

    Page<ChallengeGetDto> get(Pageable pageable);

    ChallengeGetDto create(ChallengePostDto challengeRequestDto);

    void delete(int id);

    ChallengeGetDto getById(int id);

    ChallengeGetDto partialUpdate(int id, JsonMergePatch patch);

}
