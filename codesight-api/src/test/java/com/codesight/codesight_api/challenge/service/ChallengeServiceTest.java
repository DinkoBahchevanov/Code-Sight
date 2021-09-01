package com.codesight.codesight_api.challenge.service;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.domain.challenge.repository.ChallengeRepository;
import com.codesight.codesight_api.domain.challenge.service.ChallengeService;
import com.codesight.codesight_api.domain.challenge.service.ChallengeServiceImpl;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.InvalidPointsRangeException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.shared.IdCannotBeChangedException;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.codesight.codesight_api.web.mappers.ChallengeMapper;
import com.codesight.codesight_api.web.mappers.ChallengeMapperImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.codesight.codesight_api.domain.challenge.entity.Difficulty.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ChallengeServiceTest {

    private ChallengeRepository challengeRepository;
    private ObjectMapper objectMapper;
    private ChallengeMapper challengeMapper;
    private ChallengeService challengeService;
    private Challenge challenge;
    private ChallengePostDto postDto;

    @BeforeEach
    void setUp() {
        challengeRepository = mock(ChallengeRepository.class);
        objectMapper = new ObjectMapper();
        challengeMapper = new ChallengeMapperImpl();
        challengeService = new ChallengeServiceImpl(challengeRepository, objectMapper,challengeMapper);
    }

    @BeforeEach
    public void initBeforeAll() {
        this.challenge = new Challenge(1,"baba", "baba", EASY, 2);
        this.postDto = new ChallengePostDto("baba", "baba", EASY, 2);
    }

    @Test
    void getShouldReturnAllChallenges() {
        Challenge challengePostDto1 = new Challenge("a", "aaadsd", EASY, 2);
        Challenge challengePostDto2 = new Challenge("aa", "aaadsa", MEDIUM, 2);
        Challenge challengePostDto3 = new Challenge("aaa", "aaasda", HARD, 2);

        ArrayList<Challenge> list = new ArrayList<>();
        list.add(challengePostDto1);
        list.add(challengePostDto2);
        list.add(challengePostDto3);

        final Page<Challenge> challenges = new PageImpl<>(list);

        Pageable pageable = PageRequest.of(0, 3);


        when(challengeRepository.findAll(pageable)).thenReturn(challenges);

        Page<ChallengeGetDto> returnedChallenges = challengeService.get(pageable);

        assertThat(returnedChallenges).usingRecursiveComparison().isEqualTo(challenges);
    }

    @Test
    void getShouldNotReturnAnyUsers() {
        ArrayList<Challenge> list = new ArrayList<>();

        Pageable firstPageWithTwoChallenges = PageRequest.of(0, 10);
        final Page<Challenge> challenges = new PageImpl<>(list);

        when(challengeRepository.findAll(firstPageWithTwoChallenges)).thenReturn(challenges);
        Page<ChallengeGetDto> returnedChallenges = challengeService.get(firstPageWithTwoChallenges);
        assertEquals(0, returnedChallenges.getSize());
    }

    @Test
    void getByIdShouldReturnChallenge() {
        Optional<Challenge> originalChallenge = Optional.of(challenge);

        when(challengeRepository.findById(1)).thenReturn(originalChallenge);

        ChallengeGetDto expected = challengeMapper.mapChallengeToChallengeGetDto(originalChallenge.get());

        assertThat(challengeService.getById(1)).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void getByIdShouldThrowException() {
        when(challengeRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(ChallengeNotFoundException.class, () ->{
            challengeService.getById(1);
        });

    }

    @Test
    void deleteShouldDeleteChallenge() {
        when(challengeRepository.findById(1)).thenReturn(Optional.of(challenge));
        challengeService.delete(1);
        verify(challengeRepository).deleteById(challenge.getId());
    }

    @Test
    void deleteShouldThrowExceptionWhenUserNotFound() {
        Optional<Challenge> challenge = Optional.empty();
        when(challengeRepository.findById(1)).thenReturn(challenge);
        assertThrows(ChallengeNotFoundException.class, () -> {
            challengeService.delete(1);
        });
    }

    @Test
    void createShouldCreateChallenge() {
        when(challengeRepository.save(ArgumentMatchers.any(Challenge.class))).thenReturn(challenge);
        ChallengeGetDto created = challengeService.create(postDto);

        assertThat(created).usingRecursiveComparison().isEqualTo(challenge);
    }

    @Test
    void patchShouldThrowException() throws JsonProcessingException, JsonPatchException {
        String jsonString = "{\n" +
                "  \"difficulty\": \"EASY\"\n" +
                "}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonMergePatch patch = JsonMergePatch.fromJson(jsonNode);

        when(challengeRepository.findById(1)).thenThrow(ChallengeNotFoundException.class);
        assertThrows(ChallengeNotFoundException.class, () -> {
            challengeService.partialUpdate(1, patch);
        });
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForChallengePatch")
    void patchShouldUpdateChallenge(String jsonString, Challenge challengeAfterUpdate) throws JsonPatchException, JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonMergePatch patch = JsonMergePatch.fromJson(jsonNode);
        Challenge beforeUpdate = new Challenge("Dinko", "Bahchevanov", EASY, 3);
        beforeUpdate.setId(1);

        when(challengeRepository.findById(1)).thenReturn(Optional.of(beforeUpdate));
        challengeService.partialUpdate(1, patch);

        ArgumentCaptor<Challenge> challengeArgumentCaptor = ArgumentCaptor.forClass(Challenge.class);

        verify(challengeRepository).save(challengeArgumentCaptor.capture());
        Challenge capturedChallenge = challengeArgumentCaptor.getValue();

        assertThat(capturedChallenge).usingRecursiveComparison().isEqualTo(challengeAfterUpdate);
    }

    @Test
    void willThrowWhenTryingToChangeId() throws JsonProcessingException, JsonPatchException {
        Challenge challenge = new Challenge(1,"baba", "baba", EASY, 2);
        when(challengeRepository.findById(anyInt())).thenReturn(Optional.of(challenge));

        String jsonString = "{\"id\": \"5\"}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonMergePatch patch = JsonMergePatch.fromJson(jsonNode);

        Assertions.assertThrows(IdCannotBeChangedException.class, ()->{
            challengeService.partialUpdate(challenge.getId(), patch);
        });

    }
    @Test
    void willThrowWhenJsonFormatIsWrong() throws JsonProcessingException, JsonPatchException {
        when(challengeRepository.findById(anyInt())).thenReturn(Optional.of(challenge));

        String jsonString = "{\"difficulty\": \"Baba\"}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonMergePatch patch = JsonMergePatch.fromJson(jsonNode);

        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            challengeService.partialUpdate(challenge.getId(), patch);
        });
    }

    private static List<Arguments> provideArgumentsForChallengePatch(){
        return List.of(
                Arguments.of("{\n" +
                        "  \"name\": \"JABOKA DINKO\",\n" +
                        "  \"points\": 4\n" +
                        "}", new Challenge(1,"JABOKA DINKO", "Bahchevanov", EASY, 4)),

                Arguments.of("{\n" +
                        "  \"description\": \"JABOK\",\n" +
                        "  \"difficulty\": \"EASY\"\n" +
                        "}",  new Challenge(1,"Dinko", "JABOK", EASY, 3)),

                Arguments.of("{\n" +
                        "  \"difficulty\": \"MEDIUM\",\n" +
                        "  \"name\": \"JABOKA DINKO\",\n" +
                        "  \"points\": 125\n" +
                        "}", new Challenge(1,"JABOKA DINKO", "Bahchevanov", MEDIUM, 125)),

                Arguments.of("{\n" +
                        "  \"difficulty\": \"EASY\"\n" +
                        "}", new Challenge(1,"Dinko", "Bahchevanov", EASY, 3))
        );
    }

    @Test
    void willThrowExceptionWhenTryToSetDifferentLevelToPoints() throws JsonProcessingException, JsonPatchException {
        String jsonString = "{\n" +
                "  \"difficulty\": \"HARD\"\n" +
                "}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonMergePatch patch = JsonMergePatch.fromJson(jsonNode);

        when(challengeRepository.findById(1)).thenReturn(Optional.ofNullable(challenge));

        assertThrows(InvalidPointsRangeException.class, () -> {
            challengeService.partialUpdate(1, patch);
        });
    }

}
