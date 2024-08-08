package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}