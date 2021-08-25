package com.codesight.codesight_api.challenges;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.domain.challenge.entity.Difficulty;
import com.codesight.codesight_api.domain.challenge.repository.ChallengeRepository;
import com.codesight.codesight_api.domain.challenge.service.ChallengeService;
import com.codesight.codesight_api.domain.challenge.service.ChallengeServiceImpl;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.codesight.codesight_api.web.mappers.ChallengeMapper;
import com.codesight.codesight_api.web.mappers.ChallengeMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ChallengesServiceTest {

    private ChallengeService challengeService;
    private ChallengeRepository challengeRepository;
    private ChallengeMapper challengeMapper;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        challengeMapper = new ChallengeMapperImpl();
        challengeRepository = mock(ChallengeRepository.class);
        objectMapper = new ObjectMapper();
        challengeService = new ChallengeServiceImpl(challengeRepository, challengeMapper, objectMapper);
    }

    @Test
    void getShouldReturnAllChallenges() {
        Challenge challengePostDto1 = new Challenge("aa", "aaadsd", Difficulty.EASY, 2);
        Challenge challengePostDto2 = new Challenge("aa", "aaadsa", Difficulty.MEDIUM, 2);
        Challenge challengePostDto3 = new Challenge("aa", "aaasda", Difficulty.HARD, 2);

        ArrayList<Challenge> list = new ArrayList<>();
        list.add(challengePostDto1);
        list.add(challengePostDto2);
        list.add(challengePostDto3);

        Pageable firstPageWithTwoChallenges = PageRequest.of(0,10);
        final Page<Challenge> challenges = new PageImpl<>(list);

        when(challengeRepository.findAll(firstPageWithTwoChallenges)).thenReturn(challenges);

        ArrayList<ChallengeGetDto> returnedChallenges = challengeService.get();

        assertEquals(3, returnedChallenges.size());
    }
}
