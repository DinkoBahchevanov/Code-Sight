package com.codesight.codesight_api.domain.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.domain.challenge.entity.Difficulty;
import com.codesight.codesight_api.domain.challenge.repository.ChallengeRepository;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.InvalidPointsRangeException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.shared.IdCannotBeChangedException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.shared.IncorrectJsonMergePatchProcessingException;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.codesight.codesight_api.web.mappers.ChallengeMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static com.codesight.codesight_api.domain.challenge.entity.Difficulty.*;


@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ObjectMapper objectMapper;
    private final ChallengeMapper challengeMapper;

    @Autowired
    public ChallengeServiceImpl(ChallengeRepository challengeRepository, ObjectMapper objectMapper, ChallengeMapper challengeMapper) {
        this.challengeRepository = challengeRepository;
        this.objectMapper = objectMapper;
        this.challengeMapper = challengeMapper;
    }

    @Override
    public Page<ChallengeGetDto> get(Pageable pageable) {
        Page<Challenge> all = challengeRepository.findAll(pageable);
        return all.map(challengeMapper::mapChallengeToChallengeGetDto);
    }

    @Override
    public ChallengeGetDto getById(int id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() ->
                new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id)));

        return challengeMapper.mapChallengeToChallengeGetDto(challenge);
    }

    @Override
    public ChallengeGetDto create(ChallengePostDto challengePostDto) {
        validatePoints(challengePostDto.getPoints(), challengePostDto.getDifficulty());
        Challenge challenge = challengeRepository.save(challengeMapper.mapChallengePostDtoToChallenge(challengePostDto));

        return challengeMapper.mapChallengeToChallengeGetDto(challenge);
    }

    private void validatePoints(int points, Difficulty difficulty) {
        if     ((points <= 100 && difficulty != EASY) ||
                (points >= 101 && points <= 200 && difficulty != MEDIUM) ||
                (points >= 201 && difficulty != HARD)) {
            throw new InvalidPointsRangeException(points, difficulty);
        }
    }

    @Override
    public void delete(int id) {
        challengeRepository.findById(id).orElseThrow(() ->
                new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id)));

        challengeRepository.deleteById(id);
    }

    @Override
    public ChallengeGetDto partialUpdate(int id, JsonMergePatch patch) {
        Challenge originalChallenge = challengeRepository.findById(id).orElseThrow(() ->
                new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id)));

        Challenge challengePatched = null;
        try {
            challengePatched = applyPatchToChallenge(patch, originalChallenge);

            if (challengePatched.getId() != originalChallenge.getId()) {
                throw new IdCannotBeChangedException();
            }

            return challengeMapper.mapChallengeToChallengeGetDto(challengeRepository.save(challengePatched));
        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
            throw new IncorrectJsonMergePatchProcessingException("There's a problem with the format of the request, check if the passed enum exists");
        }
    }

    private Challenge applyPatchToChallenge(JsonMergePatch patch, Challenge originalChallenge) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(originalChallenge, JsonNode.class));
        Difficulty difficulty = Difficulty.valueOf(String.valueOf(patched.get("difficulty"))
                .substring(1, String.valueOf(patched.get("difficulty")).length() - 1));
        validatePoints(Integer.parseInt(String.valueOf(patched.get("points"))), difficulty);

        return objectMapper.treeToValue(patched, Challenge.class);
    }
}
