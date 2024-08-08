package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.MarkUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkUserRepository extends JpaRepository<MarkUser, Long> {
}