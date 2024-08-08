package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.CodesMail;
import org.example.onlineaudiobook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodesMailRepository extends JpaRepository<CodesMail, Long> {
    Optional<CodesMail> findByUserId(Long aLong);
    void deleteAllByUserId(Long id);

}