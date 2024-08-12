package org.example.onlineaudiobook.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.onlineaudiobook.entity.enums.BookType;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String name;
    @Lob
    private byte[] content;
}
