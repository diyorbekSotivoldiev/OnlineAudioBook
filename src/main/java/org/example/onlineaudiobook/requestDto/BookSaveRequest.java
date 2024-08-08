package org.example.onlineaudiobook.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookSaveRequest {
    private String bookName;
    private String authorName;
    private String type;
    private Long bookCategoryId;
}
