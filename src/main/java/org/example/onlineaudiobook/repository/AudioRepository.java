package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.Audio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioRepository extends JpaRepository<Audio, Long> {
}