package com.codesight.codesight_api.challenges;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.domain.challenge.entity.Difficulty;
import com.codesight.codesight_api.domain.challenge.repository.ChallengeRepository;
import com.codesight.codesight_api.domain.challenge.service.ChallengeService;
import com.codesight.codesight_api.domain.challenge.service.ChallengeServiceImpl;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import com.codesight.codesight_api.web.mappers.ChallengeMapper;
import com.codesight.codesight_api.web.mappers.ChallengeMapperImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.apache.johnzon.core.JsonProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import javax.json.*;
import javax.json.bind.JsonbBuilder;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static com.codesight.codesight_api.domain.challenge.entity.Difficulty.EASY;
import static com.codesight.codesight_api.domain.challenge.entity.Difficulty.HARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        Challenge challengePostDto1 = new Challenge("a", "aaadsd", EASY, 2);
        Challenge challengePostDto2 = new Challenge("aa", "aaadsa", Difficulty.MEDIUM, 2);
        Challenge challengePostDto3 = new Challenge("aaa", "aaasda", HARD, 2);

        ArrayList<Challenge> list = new ArrayList<>();
        list.add(challengePostDto1);
        list.add(challengePostDto2);
        list.add(challengePostDto3);

        Pageable firstPageWithTwoChallenges = PageRequest.of(0,10, Sort.by("name").ascending());
        final Page<Challenge> challenges = new PageImpl<>(list);

        when(challengeRepository.findAll(firstPageWithTwoChallenges)).thenReturn(challenges);

        Page<ChallengeGetDto> returnedChallenges = challengeService.get(0,10,"name", "ASC");

        assertEquals(challenges.getSize(), returnedChallenges.getSize());
    }

    @Test
    void getShouldReturnOnlyTwoChallenges() {
        Challenge challengePostDto1 = new Challenge("a", "aaadsd", EASY, 2);
        Challenge challengePostDto2 = new Challenge("aa", "aaadsa", Difficulty.MEDIUM, 2);
        Challenge challengePostDto3 = new Challenge("aaa", "aaasda", HARD, 2);

        ArrayList<Challenge> list = new ArrayList<>();
        list.add(challengePostDto1);
        list.add(challengePostDto2);
        list.add(challengePostDto3);

        Pageable firstPageWithTwoChallenges = PageRequest.of(0,2, Sort.by("name").ascending());
        final Page<Challenge> challenges = new PageImpl<>(list);

        when(challengeRepository.findAll(firstPageWithTwoChallenges)).thenReturn(challenges);

        Page<ChallengeGetDto> returnedChallenges = challengeService.get(0,2,"name", "ASC");

        assertEquals(challenges.getSize(), returnedChallenges.getSize());
    }

    @Test
    void getShouldReturnSortedChallenges() {
        Challenge challengePostDto1 = new Challenge("a", "aaadsd", EASY, 2);
        Challenge challengePostDto2 = new Challenge("aa", "aaadsa", Difficulty.MEDIUM, 2);
        Challenge challengePostDto3 = new Challenge("aaa", "aaasda", HARD, 2);

        ArrayList<Challenge> list = new ArrayList<>();
        list.add(challengePostDto1);
        list.add(challengePostDto2);
        list.add(challengePostDto3);

        Pageable firstPageWithTwoChallenges = PageRequest.of(0,10, Sort.by("name").descending());
        final Page<Challenge> challenges = new PageImpl<>(list);

        when(challengeRepository.findAll(firstPageWithTwoChallenges)).thenReturn(challenges);

        Page<ChallengeGetDto> returnedChallenges = challengeService.get(0,10,"name", "DESC");

        assertEquals(returnedChallenges.getContent().get(2).getName(), "aaa");
    }

    @Test
    void getShouldNotReturnAnyUsers() {
        ArrayList<Challenge> list = new ArrayList<>();

        Pageable firstPageWithTwoChallenges = PageRequest.of(0,10, Sort.by("name"));
        final Page<Challenge> challenges = new PageImpl<>(list);

        when(challengeRepository.findAll(firstPageWithTwoChallenges)).thenReturn(challenges);
        Page<ChallengeGetDto> returnedChallenges = challengeService.get(0,10,"name","ASC");
        assertEquals(0, returnedChallenges.getSize());
    }

    @Test
    void getByIdShouldReturnChallenge() {
        Optional<Challenge> originalChallenge = Optional.of(new Challenge("baba", "baba", EASY, 2));
        originalChallenge.get().setId(1);
        when(challengeRepository.findById(1)).thenReturn(originalChallenge);
        ChallengeGetDto expected = challengeMapper.mapChallengeToChallengeGetDto(originalChallenge.get());

        assertEquals(expected.getName(), challengeService.getById(1).getName());
    }

    @Test
    void getByIdShouldThrowException() {
        Optional<Challenge> challenge = Optional.empty();
        when(challengeRepository.findById(1)).thenReturn(challenge);
        assertThrows(ChallengeNotFoundException.class, () -> {
            challengeService.getById(1);
        });
    }

    @Test
    void deleteShouldDeleteChallenge() {
        Challenge challenge = new Challenge("baba", "baba", EASY, 2);
        challenge.setId(1);

        when(challengeRepository.findById(1)).thenReturn(Optional.of(challenge));
        challengeService.delete(1);
        verify(challengeRepository).deleteById(challenge.getId());
    }

    @Test
    void deleteShouldThrowException() {
        Optional<Challenge> challenge = Optional.empty();
        when(challengeRepository.findById(1)).thenReturn(challenge);
        assertThrows(ChallengeNotFoundException.class, () -> {
            challengeService.delete(1);
        });
    }

    @Test
    void createShouldCreateChallenge() {
        Challenge challenge = new Challenge("baba", "baba", EASY, 2);
        challenge.setId(1);
        when(challengeRepository.save(ArgumentMatchers.any(Challenge.class))).thenReturn(challenge);
        ChallengeGetDto created = challengeService.create(new ChallengePostDto("baba", "baba", EASY, 2));
        assertEquals(created.getName(), challenge.getName());
    }

    @Test
    void updateShouldUpdateChallenge() {
        Challenge challenge = new Challenge("baba", "baba", EASY, 2);
        challenge.setId(1);

        when(challengeRepository.findById(1)).thenReturn(Optional.of(challenge));

        Challenge newChallenge = new Challenge("meca", "jaba", HARD, 3);
        newChallenge.setId(1);

        when(challengeRepository.save(ArgumentMatchers.any(Challenge.class))).thenReturn(newChallenge);
        ChallengeGetDto challengeGetDto = challengeService.update(1,new ChallengePostDto("baba", "baba", EASY, 2));

        assertEquals(challengeGetDto.getName(), newChallenge.getName());
        assertEquals(challengeGetDto.getDescription(), newChallenge.getDescription());
        assertEquals(challengeGetDto.getPoints(), newChallenge.getPoints());
        assertEquals(challengeGetDto.getDifficulty(), newChallenge.getDifficulty());
    }

    @Test
    void updateShouldThrowException() {
        when(challengeRepository.findById(1)).thenThrow(ChallengeNotFoundException.class);
        assertThrows(ChallengeNotFoundException.class, () -> {
            challengeService.update(1, new ChallengePostDto());
        });
    }

    @Test
    void patchShouldThrowException() {
        when(challengeRepository.findById(1)).thenThrow(ChallengeNotFoundException.class);
        assertThrows(ChallengeNotFoundException.class, () -> {
            challengeService.partialUpdate(1, new JsonPatch(new ArrayList<>()));
        });
    }

    @Test
    void patchShouldUpdateChallenge() throws JsonPatchException, JsonProcessingException {

        JsonReader reader = Json.createReader(Challenge.class.getResourceAsStream("/challenge.json"));
        JsonArray jsonaArray = reader.readArray();

        javax.json.JsonPatch patch = Json.createPatchBuilder()
                .replace("/0/name", "Duke Oracle")
                .remove("/1")
                .build();

        JsonArray result = patch.apply(jsonaArray);
        System.out.println(result.toString());

        Type type = new ArrayList<Challenge>() {}.getClass().getGenericSuperclass();

        List<Challenge> person = JsonbBuilder.create().fromJson(result.toString(), type);
        assertEquals("Duke Oracle", person.get(0).getName());
//        Challenge challenge = new Challenge("meca", "jaba", HARD, 3);
//        challenge.setId(1);
//
//        JsonNode jsonNode = objectMapper.convertValue(challenge, JsonNode.class);
//        when(challengeRepository.findById(1)).thenReturn(Optional.of(challenge));
//
//        when(this.jsonPatch.apply(objectMapper.convertValue(challenge, JsonNode.class))).thenReturn(jsonNode);
//
//        assertEquals(objectMapper.treeToValue(jsonNode, Challenge.class).getName(), challenge.getName());
    }
}
