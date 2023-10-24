package com.ssafy.journeymate.journeyservice.repository;

import com.ssafy.journeymate.journeyservice.entity.JourneyEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyRepository extends JpaRepository<JourneyEntity, Long> {

    List<JourneyEntity> findAllByMateId(Long mateId);

    Optional<JourneyEntity> findById(Long journeyId);





}