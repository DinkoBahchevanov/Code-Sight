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
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.codesight.codesight_api.domain.challenge.entity.Difficulty.EASY;
import static com.codesight.codesight_api.domain.challenge.entity.Difficulty.HARD;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ChallengesServiceTest {

    private ChallengeService challengeService;
    private ChallengeRepository challengeRepository;
    private ObjectMapper objectMapper;
    private ChallengeMapper challengeMapper;

    @BeforeEach
    void setUp() {
        challengeRepository = mock(ChallengeRepository.class);
        objectMapper = new ObjectMapper();
        challengeMapper = new ChallengeMapperImpl();
        challengeService = new ChallengeServiceImpl(challengeRepository, objectMapper,challengeMapper);
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

        final Page<Challenge> challenges = new PageImpl<>(list);

        Pageable firstPageWithTwoChallenges = PageRequest.of(0, 3, Sort.by("name").ascending());


        when(challengeRepository.findAll(firstPageWithTwoChallenges)).thenReturn(challenges);

        Page<ChallengeGetDto> returnedChallenges = challengeService.get(firstPageWithTwoChallenges);

        assertEquals(challenges.getSize(), returnedChallenges.getSize());
    }

    @Test
    void getShouldReturnOnlyTwoChallengesSortedByName() {
        Challenge challengePostDto1 = new Challenge("a", "aaadsd", EASY, 2);
        Challenge challengePostDto2 = new Challenge("aa", "aaadsa", Difficulty.MEDIUM, 2);
        Challenge challengePostDto3 = new Challenge("aaa", "aaasda", HARD, 2);

        ArrayList<Challenge> list = new ArrayList<>();
        list.add(challengePostDto1);
        list.add(challengePostDto2);
        list.add(challengePostDto3);

        Pageable firstPageWithTwoChallenges = PageRequest.of(0, 2, Sort.by("name").ascending());
        final Page<Challenge> challenges = new PageImpl<>(list);

        when(challengeRepository.findAll(firstPageWithTwoChallenges)).thenReturn(challenges);

        Page<ChallengeGetDto> returnedChallenges = challengeService.get(firstPageWithTwoChallenges);

        assertEquals(challenges.getSize(), returnedChallenges.getSize());
    }
//
    @Test
    void getShouldReturnSortedChallenges() {
        Challenge challengePostDto1 = new Challenge("a", "aaadsd", EASY, 2);
        Challenge challengePostDto2 = new Challenge("aa", "aaadsa", Difficulty.MEDIUM, 2);
        Challenge challengePostDto3 = new Challenge("aaa", "aaasda", HARD, 2);

        ArrayList<Challenge> list = new ArrayList<>();
        list.add(challengePostDto1);
        list.add(challengePostDto2);
        list.add(challengePostDto3);

        Pageable firstPageWithTwoChallenges = PageRequest.of(0, 10, Sort.by("name").descending());
        final Page<Challenge> challenges = new PageImpl<>(list);

        when(challengeRepository.findAll(firstPageWithTwoChallenges)).thenReturn(challenges);

        Page<ChallengeGetDto> returnedChallenges = challengeService.get(firstPageWithTwoChallenges);

        assertEquals(returnedChallenges.getContent().get(2).getName(), "aaa");
    }

    @Test
    void getShouldNotReturnAnyUsers() {
        ArrayList<Challenge> list = new ArrayList<>();

        Pageable firstPageWithTwoChallenges = PageRequest.of(0, 10, Sort.by("name"));
        final Page<Challenge> challenges = new PageImpl<>(list);

        when(challengeRepository.findAll(firstPageWithTwoChallenges)).thenReturn(challenges);
        Page<ChallengeGetDto> returnedChallenges = challengeService.get(firstPageWithTwoChallenges);
        assertEquals(0, returnedChallenges.getSize());
    }

    @Test
    void getByIdShouldReturnChallenge() {
        Optional<Challenge> originalChallenge = Optional.of(new Challenge("baba", "baba", EASY, 2));
        originalChallenge.get().setId(1);
        when(challengeRepository.findById(1)).thenReturn(originalChallenge);

        ChallengeGetDto expected = challengeMapper.mapChallengeToChallengeGetDto(originalChallenge.get());

        assertThat(challengeService.getById(1)).usingRecursiveComparison().isEqualTo(expected);
    }
//
    @Test
    void getByIdShouldThrowException() {
        Optional<Challenge> challenge = Optional.empty();
        when(challengeRepository.findById(1)).thenReturn(challenge);
        assertThrows(ChallengeNotFoundException.class, () -> {
            challengeService.getById(1);
        });
    }
//
    @Test
    void deleteShouldDeleteChallenge() {
        Challenge challenge = new Challenge("baba", "baba", EASY, 2);
        challenge.setId(1);

        when(challengeRepository.findById(1)).thenReturn(Optional.of(challenge));
        challengeService.delete(1);
        verify(challengeRepository).deleteById(challenge.getId());
    }
//
    @Test
    void deleteShouldThrowException() {
        Optional<Challenge> challenge = Optional.empty();
        when(challengeRepository.findById(1)).thenReturn(challenge);
        assertThrows(ChallengeNotFoundException.class, () -> {
            challengeService.delete(1);
        });
    }
//
    @Test
    void createShouldCreateChallenge() {
        Challenge challenge = new Challenge("baba", "baba", EASY, 2);
        challenge.setId(1);
        when(challengeRepository.save(ArgumentMatchers.any(Challenge.class))).thenReturn(challenge);
        ChallengeGetDto created = challengeService.create(new ChallengePostDto("baba", "baba", EASY, 2));
        assertEquals(created.getName(), challenge.getName());
    }
//
    @Test
    void updateShouldUpdateChallenge() {
        Challenge challenge = new Challenge("baba", "baba", EASY, 2);
        challenge.setId(1);

        when(challengeRepository.findById(1)).thenReturn(Optional.of(challenge));

        Challenge newChallenge = new Challenge("meca", "jaba", HARD, 3);
        newChallenge.setId(1);

        when(challengeRepository.save(ArgumentMatchers.any(Challenge.class))).thenReturn(newChallenge);
        ChallengeGetDto challengeGetDto = challengeService.update(1, new ChallengePostDto("meca", "jaba", HARD, 3));

        assertThat(challengeGetDto).usingRecursiveComparison().isEqualTo(newChallenge);
    }
//
    @Test
    void updateShouldThrowException() {
        when(challengeRepository.findById(1)).thenThrow(ChallengeNotFoundException.class);
        assertThrows(ChallengeNotFoundException.class, () -> {
            challengeService.update(1, new ChallengePostDto());
        });
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
    @MethodSource("provideArgumentsForPatch")
    void patchShouldUpdateChallenge(String jsonString, Challenge challengeAfterUpdate) throws JsonPatchException, JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonMergePatch patch = JsonMergePatch.fromJson(jsonNode);
        Challenge beforeUpdate = new Challenge("Dinko", "Bahchevanov", HARD, 3);
        beforeUpdate.setId(1);

        when(challengeRepository.findById(1)).thenReturn(Optional.of(beforeUpdate));
        challengeService.partialUpdate(1, patch);

        ArgumentCaptor<Challenge> challengeArgumentCaptor = ArgumentCaptor.forClass(Challenge.class);

        verify(challengeRepository).save(challengeArgumentCaptor.capture());
        Challenge capturedChallenge = challengeArgumentCaptor.getValue();

        assertThat(capturedChallenge).usingRecursiveComparison().isEqualTo(challengeAfterUpdate);
    }

    private static List<Arguments> provideArgumentsForPatch(){
        return List.of(
                Arguments.of("{\n" +
                        "  \"name\": \"JABOKA DINKO\",\n" +
                        "  \"points\": 123\n" +
                        "}", new Challenge(1,"JABOKA DINKO", "Bahchevanov", HARD, 123)),

                Arguments.of("{\n" +
                        "  \"description\": \"JABOK\",\n" +
                        "  \"difficulty\": \"EASY\"\n" +
                        "}",  new Challenge(1,"Dinko", "JABOK", EASY, 3)),

                Arguments.of("{\n" +
                        "  \"difficulty\": \"EASY\",\n" +
                        "  \"name\": \"JABOKA DINKO\",\n" +
                        "  \"points\": 125\n" +
                        "}", new Challenge(1,"JABOKA DINKO", "Bahchevanov", EASY, 125)),

                Arguments.of("{\n" +
                        "  \"difficulty\": \"EASY\"\n" +
                        "}", new Challenge(1,"Dinko", "Bahchevanov", EASY, 3))
        );
    }
}
