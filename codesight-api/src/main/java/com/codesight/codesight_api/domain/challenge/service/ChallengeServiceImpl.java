package com.codesight.codesight_api.domain.challenge.service;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.domain.challenge.repository.ChallengeRepository;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeAlreadyExistsException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeIdCannotBeChangedException;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.mappers.ChallengeMapper;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;
    private final ObjectMapper objectMapper;

    public ChallengeServiceImpl(ChallengeRepository challengeRepository, ChallengeMapper challengeMapper, ObjectMapper objectMapper) {
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public Page<ChallengeGetDto> get(int pageNumber, int pageSize, String sortBy, String direction) {
        Sort sortOrder = Sort.by(sortBy);
        Pageable paging;
        switch (direction) {
            case "DESC":
                paging = PageRequest.of(pageNumber, pageSize, sortOrder.descending());
                break;
            case "ASC":
            default: paging = PageRequest.of(pageNumber, pageSize, sortOrder.ascending());
        }

        Page<ChallengeGetDto> pageResult = challengeRepository.findAll(paging).map(challengeMapper::mapChallengeToChallengeGetDto);

        return pageResult;
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
        try {
            Challenge challenge = challengeRepository.save(challengeMapper.mapChallengePostDtoToChallenge(challengePostDto));
            return challengeMapper.mapChallengeToChallengeGetDto(challenge);
        } catch (Exception ex) {
            throw new ChallengeAlreadyExistsException(String.format("Challenge with name '%s' already exists", challengePostDto.getName()));
        }
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
        try {
            return challengeMapper.mapChallengeToChallengeGetDto(challengeRepository.save(existingChallenge));
        } catch (Exception ex) {
            throw new ChallengeAlreadyExistsException(String.format("Challenge with name '%s' already exists", challengePostDto.getName()));
        }
    }
    @Override
    public ChallengeGetDto partialUpdate(int id, JsonPatch patch) {
        if (!challengeRepository.findById(id).isPresent()) {
            throw new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id));
        }
        Challenge originalChallenge = challengeRepository.findById(id).get();
        Challenge challengePatched = applyPatchToChallenge(patch, originalChallenge);

        if (challengePatched.getId() != originalChallenge.getId())
            throw new ChallengeIdCannotBeChangedException("Cannot change id on challenge!");

        challengeRepository.save(challengePatched);
        return challengeMapper.mapChallengeToChallengeGetDto(challengeRepository.save(challengePatched));
    }

    private Challenge applyPatchToChallenge(JsonPatch patch, Challenge originalChallenge) {
        JsonNode patched = null;
        try {
            patched = patch.apply(objectMapper.convertValue(originalChallenge, JsonNode.class));
        } catch (JsonPatchException e) {
            e.printStackTrace();
        }
        try {
            return objectMapper.treeToValue(patched, Challenge.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
