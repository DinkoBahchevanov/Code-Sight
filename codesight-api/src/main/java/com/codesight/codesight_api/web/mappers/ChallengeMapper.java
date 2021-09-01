package com.codesight.codesight_api.web.mappers;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.challenge.ChallengePostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChallengeMapper {

    Challenge mapChallengePostDtoToChallenge(ChallengePostDto challengePostDto);

    ChallengeGetDto mapChallengeToChallengeGetDto(Challenge challenge);
}
