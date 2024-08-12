package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneOrUsernameOrEmail(String phone, String username, String email);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);
}