package com.ssafy.journeymate.mateservice.repository;

import com.ssafy.journeymate.mateservice.entity.DocsImg;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocsImgRepository extends JpaRepository<DocsImg, Long> {

    Optional<List<DocsImg>> findByDocs_id(Long docsId);
}
