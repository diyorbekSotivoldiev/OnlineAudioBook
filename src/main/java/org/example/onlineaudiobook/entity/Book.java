package org.example.onlineaudiobook.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.onlineaudiobook.entity.enums.BookType;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookName;
    private String authorName;
    @CreatedDate
    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    private BookType type;

    @ManyToOne
    private BookCategory bookCategory;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    private Audio audio;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PdfBook pdfBook;
}
