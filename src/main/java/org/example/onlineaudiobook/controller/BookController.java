package org.example.onlineaudiobook.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.enums.BookType;
import org.example.onlineaudiobook.repository.BookRepository;
import org.example.onlineaudiobook.responseDto.BookResponseDTO;
import org.example.onlineaudiobook.service.BookService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/getAll")
    public HttpEntity<?> getAllBooks() {
        List<BookResponseDTO> allBooks = bookService.getAllBooks();

        return ResponseEntity.ok(allBooks);
    }

    @GetMapping("/categoryId/{categoryId}")
    public HttpEntity<?> findByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(bookService.getByCategoryId(categoryId));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @GetMapping("/search/{text}")
    public HttpEntity<?> searchBook(@PathVariable String text) {
        return ResponseEntity.ok(bookService.searchBook(text));
    }


    @Transactional
    @PostMapping("/create")
    public HttpEntity<?> createBook(
            @RequestParam("bookName") String bookName,
            @RequestParam("authorName") String authorName,
            @RequestParam("type") BookType type,
            @RequestParam("bookCategoryId") Long bookCategoryId,
            @RequestParam("audioFile") @NotNull MultipartFile audioFile,
            @RequestParam("pdfBookFile") @NotNull MultipartFile pdfBookFile) {
        try {
            if (!Objects.equals(audioFile.getContentType(), "audio/mpeg"))
                return new ResponseEntity<>("Invalid audio file type. Only MP3 is allowed.", HttpStatus.BAD_REQUEST);

            if (!Objects.equals(pdfBookFile.getContentType(), "application/pdf"))
                return new ResponseEntity<>("Invalid PDF file type. Only PDF is allowed.", HttpStatus.BAD_REQUEST);

            bookService.saveBook(bookName, authorName, type, bookCategoryId, audioFile, pdfBookFile);
            return new ResponseEntity<>("book", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
