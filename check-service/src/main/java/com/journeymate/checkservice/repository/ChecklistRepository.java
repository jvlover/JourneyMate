package com.journeymate.checkservice.repository;

import com.journeymate.checkservice.entity.Checklist;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    Optional<Checklist> findChecklistByUserIdAndJourneyId(String userId, Long journeyId);
}
