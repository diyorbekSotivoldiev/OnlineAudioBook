package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.Book;
import org.example.onlineaudiobook.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByBook(Book book);
}