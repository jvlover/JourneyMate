package com.ssafy.journeymate.mateservice.repository;

import com.ssafy.journeymate.mateservice.entity.Docs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocsRepository extends JpaRepository<Docs, Long> {

}
