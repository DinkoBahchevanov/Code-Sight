package com.codesight.codesight_api.domain.challenge.service;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.domain.challenge.repository.ChallengeRepository;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeIdCannotBeChangedException;
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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ObjectMapper objectMapper;
    private final ChallengeMapper challengeMapper;

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
        Optional<Challenge> challenge = challengeRepository.findById(id);
        if (!challenge.isPresent()) {
            throw new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id));
        }
        return challengeMapper.mapChallengeToChallengeGetDto(challenge.get());
    }

    @Override
    public ChallengeGetDto create(ChallengePostDto challengePostDto) {
        Challenge challenge = challengeRepository.save(challengeMapper.mapChallengePostDtoToChallenge(challengePostDto));
        return challengeMapper.mapChallengeToChallengeGetDto(challenge);

    }

    @Override
    public void delete(int id) {
        if (!challengeRepository.findById(id).isPresent()) {
            throw new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id));
        }
        challengeRepository.deleteById(id);
    }

    @Override
    public ChallengeGetDto update(int id, ChallengePostDto challengePostDto) {
        if (!challengeRepository.findById(id).isPresent()) {
            throw new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id));
        }

        Challenge existingChallenge = challengeMapper.mapChallengePostDtoToChallenge(challengePostDto);
        existingChallenge.setId(id);

        return challengeMapper.mapChallengeToChallengeGetDto(challengeRepository.save(existingChallenge));
    }

    @Override
    public ChallengeGetDto partialUpdate(int id, JsonMergePatch patch) {
        if (!challengeRepository.findById(id).isPresent()) {
            throw new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id));
        }
        Challenge originalChallenge = challengeRepository.findById(id).get();
        Challenge challengePatched = null;
        try {
            challengePatched = applyPatchToChallenge(patch, originalChallenge);

            if (challengePatched.getId() == 0) {
                challengePatched.setId(id);
            }
            if (challengePatched.getId() != originalChallenge.getId()) {
                throw new ChallengeIdCannotBeChangedException("Cannot change id on challenge!");
            }
        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
        }
        assert challengePatched != null;
        return challengeMapper.mapChallengeToChallengeGetDto(challengeRepository.save(challengePatched));

    }

    private Challenge applyPatchToChallenge(JsonMergePatch patch, Challenge originalChallenge) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(originalChallenge, JsonNode.class));
        return objectMapper.treeToValue(patched, Challenge.class);
    }
}
