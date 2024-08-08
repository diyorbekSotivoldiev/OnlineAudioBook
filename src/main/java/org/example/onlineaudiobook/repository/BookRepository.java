package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}