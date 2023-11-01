package com.ssafy.journeymate.journeyservice.repository;

import com.ssafy.journeymate.journeyservice.entity.Journey;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {

    Optional<Journey> findById(Long journeyId);

    List<Journey> findAllByMateId(Long mateId);


}