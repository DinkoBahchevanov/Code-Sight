package com.codesight.codesight_api.web.dtos.challenge;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface ChallengeMapper {
    @Mappings({
            @Mapping(target="name", source="challengePostDto.name"),
            @Mapping(target="description", source="challengePostDto.description"),
            @Mapping(target="difficulty", source="challengePostDto.difficulty"),
            @Mapping(target="points", source="challengePostDto.points")
    })
    Challenge mapChallengePostDtoToChallenge(ChallengePostDto challengePostDto);

    @Mappings({
            @Mapping(target="name", source="challenge.name"),
            @Mapping(target="description", source="challenge.description"),
            @Mapping(target="difficulty", source="challenge.difficulty"),
            @Mapping(target="points", source="challenge.points")
    })
    ChallengeGetDto mapChallengeToChallengeGetDto(Challenge challenge);

    ArrayList<ChallengeGetDto> mapChallengeListToChallengeGetDtoList(ArrayList<Challenge> challenges);
}
