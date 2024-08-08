package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}