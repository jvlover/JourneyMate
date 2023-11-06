package com.ssafy.journeymate.mateservice.repository;


import com.ssafy.journeymate.mateservice.entity.Contents;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsRepository extends JpaRepository<Contents, Long> {

    List<Contents> findByMate_id(Long mateId);
}

