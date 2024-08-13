package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.Book;
import org.example.onlineaudiobook.responseDto.BookResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<BookResponseDTO> findAllByBookCategoryId(Long categoryId);
}