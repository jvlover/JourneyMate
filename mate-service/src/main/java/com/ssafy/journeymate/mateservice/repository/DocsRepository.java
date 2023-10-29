package com.ssafy.journeymate.mateservice.repository;

import com.ssafy.journeymate.mateservice.entity.Docs;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocsRepository extends JpaRepository<Docs, Long> {

    Optional<List<Docs>> findByMate_id(Long mateId);

}
