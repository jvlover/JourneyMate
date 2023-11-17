package com.journeymate.checkservice.repository;

import com.journeymate.checkservice.entity.Checklist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    List<Checklist> findChecklistByUserIdAndJourneyId(byte[] userId, Long journeyId);

    List<Checklist> findChecklistByJourneyId(Long journeyId);

}
