package com.journeymate.userservice.repository;

import com.journeymate.userservice.entity.MateBridge;
import com.journeymate.userservice.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateBridgeRepository extends JpaRepository<MateBridge, Long> {

    List<MateBridge> findByMateId(Long mateId);

    Optional<MateBridge> findByMateIdAndUser(Long mateId, User user);
}
