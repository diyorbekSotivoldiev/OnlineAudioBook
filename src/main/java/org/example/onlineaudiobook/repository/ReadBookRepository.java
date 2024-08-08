package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.ReadBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadBookRepository extends JpaRepository<ReadBook, Long> {
}