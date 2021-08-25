package com.codesight.codesight_api.web.mappers;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePartialDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
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

    @Mappings({
            @Mapping(target="name", source="challengePartialDto.name"),
            @Mapping(target="description", source="challengePartialDto.description"),
            @Mapping(target="difficulty", source="challengePartialDto.difficulty"),
            @Mapping(target="points", source="challengePartialDto.points")
    })
    ArrayList<ChallengeGetDto> mapChallengeListToChallengeGetDtoList(ArrayList<Challenge> challenges);
}
