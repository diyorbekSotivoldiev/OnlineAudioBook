package org.example.onlineaudiobook.repository;

import org.example.onlineaudiobook.entity.Book;
import org.example.onlineaudiobook.projection.BookProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByBookCategoryIdOrderByCreatedAt(Long categoryId);

    @Query(nativeQuery = true, value = """
            select
            book.id as bookId,
            book.book_name as bookName,
            book.author_name as authorName,
            book.type as bookType,
            image.content as imageContent,
            book_category.name as categoryName
            from book
            left join book_category on book.book_category_id = book_category.id
            left join image on book.image_id = image.id
            where
             LOWER(book.book_name) like LOWER(CONCAT('%', ?1, '%'))
            or
             LOWER(book_category.name) like LOWER(CONCAT('%', ?1, '%'))
             or
             LOWER(book.author_name) like LOWER(CONCAT('%', ?1, '%'))
             or
             LOWER(type) like LOWER(CONCAT('%', ?1, '%'));
            """)
    List<BookProjection> searchBook(String text);


}