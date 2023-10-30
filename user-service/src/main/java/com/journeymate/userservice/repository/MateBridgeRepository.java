package com.journeymate.userservice.repository;

import com.journeymate.userservice.entity.MateBridge;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateBridgeRepository extends JpaRepository<MateBridge, Long> {

    List<MateBridge> findByMateId(Long mateId);
    
}
