package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.PdfBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdfBookRepository extends JpaRepository<PdfBook, Long> {
}