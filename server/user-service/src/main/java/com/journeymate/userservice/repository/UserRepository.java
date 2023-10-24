package com.journeymate.userservice.repository;

import com.journeymate.userservice.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, byte[]> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

}
