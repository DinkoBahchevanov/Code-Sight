package com.codesight.codesight_api.domain.challenge.service;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.domain.challenge.repository.ChallengeRepository;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeIdCannotBeChangedException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.shared.IncorrectJsonMergePatchProcessingException;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ObjectMapper objectMapper;

    public ChallengeServiceImpl(ChallengeRepository challengeRepository, ObjectMapper objectMapper) {
        this.challengeRepository = challengeRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Page<ChallengeGetDto> get(Pageable pageable) {
            Page<ChallengeGetDto> all = challengeRepository.findAll(pageable).map(challenge -> {
                try {
                    return getChallengeDto(challenge);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return null;
            });

        return all;
    }

    @Override
    public ChallengeGetDto getById(int id) {
        try {
            Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ChallengeNotFoundException("Challenge with id '%d' not found into DB"));
            return getChallengeDto(challenge);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IncorrectJsonMergePatchProcessingException("Something went wrong with the mapping to Challenge");
        }
    }

    @Override
    public ChallengeGetDto create(ChallengePostDto challengePostDto) {
        ChallengeGetDto challengeGetDto = null;
        try {
            String dtoAsString = objectMapper.writeValueAsString(challengePostDto);
            Challenge challenge = objectMapper.readValue(dtoAsString, Challenge.class);
            getChallengeDto(challengeRepository.save(challenge));
            challengeGetDto = getChallengeDto(challenge);
            System.out.println();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return challengeGetDto;

    }

    private ChallengeGetDto getChallengeDto(Challenge challenge) throws JsonProcessingException {
        String challengeAsString = objectMapper.writeValueAsString(challenge);
        return objectMapper.readValue(challengeAsString, ChallengeGetDto.class);
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
        ChallengeGetDto challengeGetDto = null;
        if (!challengeRepository.findById(id).isPresent()) {
            throw new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id));
        }
        try {
            Challenge existingChallenge;
            String postDtoAsJson = objectMapper.writeValueAsString(challengePostDto);
            existingChallenge = objectMapper.readValue(postDtoAsJson, Challenge.class);
            existingChallenge.setId(id);
            challengeRepository.save(existingChallenge);
            challengeGetDto = getChallengeDto(existingChallenge);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return challengeGetDto;
    }

    @Override
    public ChallengeGetDto partialUpdate(int id, JsonMergePatch patch) {
        if (!challengeRepository.findById(id).isPresent()) {
            throw new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id));
        }
        Challenge originalChallenge = challengeRepository.findById(id).get();

        try {
            Challenge challengePatched = applyPatchToChallenge(patch, originalChallenge);
            challengePatched.setId(id);

            if (challengePatched.getId() != originalChallenge.getId())
                throw new ChallengeIdCannotBeChangedException("Cannot change id on challenge!");

            return getChallengeDto(challengeRepository.save(challengePatched));
        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
            throw new IncorrectJsonMergePatchProcessingException("There's a problem with the format of the request, check if the passed enum exists");
        }

    }

    private Challenge applyPatchToChallenge(JsonMergePatch patch, Challenge originalChallenge) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(originalChallenge, JsonNode.class));
        return objectMapper.treeToValue(patched, Challenge.class);
    }
}
