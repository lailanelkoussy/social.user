package com.social.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    long countByEmailOrUsername(String email, String username);

    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}
