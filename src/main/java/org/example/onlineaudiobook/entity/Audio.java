package org.example.onlineaudiobook.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.onlineaudiobook.entity.enums.BookType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private BookType type;
    private byte[] content;
}
