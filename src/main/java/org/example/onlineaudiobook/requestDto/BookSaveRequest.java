package org.example.onlineaudiobook.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.onlineaudiobook.entity.enums.BookType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookSaveRequest {
    private String bookName;
    private String authorName;
    private BookType type;
    private Long bookCategoryId;
}
