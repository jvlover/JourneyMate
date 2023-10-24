package com.ssafy.journeymate.mateservice.repository;


import com.ssafy.journeymate.mateservice.entity.Mate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateRepository extends JpaRepository<Mate, Long> {
}
