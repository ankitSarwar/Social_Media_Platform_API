package com.example.social.media.platform.API.repository;


import com.example.social.media.platform.API.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    void deleteVerificationTokenByUsername(String username);

    Optional<User> findById(Long myId);
}
