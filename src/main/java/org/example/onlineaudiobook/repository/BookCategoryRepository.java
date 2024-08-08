package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
}