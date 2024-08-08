package org.example.onlineaudiobook.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.onlineaudiobook.entity.BookCategory;
import org.example.onlineaudiobook.entity.enums.BookType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookResponseDTO {
    private String name;
    private String author;
    private BookType bookType;
    private BookCategory category;
    private byte[] image;
    private float mark;
}
