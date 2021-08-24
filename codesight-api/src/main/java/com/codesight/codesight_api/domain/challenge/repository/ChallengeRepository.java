package com.codesight.codesight_api.domain.challenge.repository;

import com.codesight.codesight_api.domain.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {

    Optional<Challenge> findByName(String challengeName);
}
