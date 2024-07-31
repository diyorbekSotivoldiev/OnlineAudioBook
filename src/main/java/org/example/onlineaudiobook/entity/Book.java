package org.example.onlineaudiobook.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    @ManyToOne
    private Audio audio;

    @OneToOne(cascade = CascadeType.ALL)
    private PdfBook pdfBook;
}
