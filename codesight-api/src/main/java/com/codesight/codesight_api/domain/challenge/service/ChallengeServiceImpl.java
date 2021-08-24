package com.codesight.codesight_api.domain.challenge.service;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.domain.challenge.repository.ChallengeRepository;
import com.codesight.codesight_api.infrastructure.exceptions.challenges.ChallengeAlreadyExistsException;
import com.codesight.codesight_api.infrastructure.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeMapper;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;

    public ChallengeServiceImpl(ChallengeRepository challengeRepository, ChallengeMapper challengeMapper) {
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
    }

    @Override
    public ArrayList<ChallengeGetDto> getChallenges() {
        return challengeMapper.mapChallengeListToChallengeGetDtoList((ArrayList<Challenge>) this.challengeRepository.findAll());
    }

    @Override
    public ChallengeGetDto create(ChallengePostDto challengePostDto) {
        if (challengeRepository.findByName(challengePostDto.getName()).isPresent()) {
            throw new ChallengeAlreadyExistsException(String.format("Challenge with name '%s' already exists", challengePostDto.getName()));
        }
        Challenge challenge = challengeRepository.save(challengeMapper.mapChallengePostDtoToChallenge(challengePostDto));
        return challengeMapper.mapChallengeToChallengeGetDto(challenge);
    }

    @Override
    public ResponseEntity<HttpStatus> delete(int id) {
        if (!challengeRepository.findById(id).isPresent()) {
            throw new ChallengeNotFoundException(String.format("Challenge with id '%d' not found into DB", id));
        }
        challengeRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
