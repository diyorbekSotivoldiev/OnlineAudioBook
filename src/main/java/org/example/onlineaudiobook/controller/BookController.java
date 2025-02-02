package org.example.onlineaudiobook.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.Book;
import org.example.onlineaudiobook.entity.Image;
import org.example.onlineaudiobook.entity.enums.BookType;
import org.example.onlineaudiobook.responseDto.BookResponseDTO;
import org.example.onlineaudiobook.service.BookService;
import org.example.onlineaudiobook.service.ImageService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api1/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final ImageService imageService;

    @GetMapping("/getAll")
    public HttpEntity<?> getAllBooks() {
        List<BookResponseDTO> allBooks = bookService.getAllBooks();

        return ResponseEntity.ok(allBooks);
    }

    @GetMapping("/image/{id}")
    public HttpEntity<?> getBookImage(@PathVariable Long id) {
        Image image = imageService.getImageById(id);
        byte[] bytes = image.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg"); //Rasm turi, masalan JPEG uchun
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"image.jpg\""); //Faylni yuklab olishda taklif qilinadigan nom
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
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


    //
//    @Operation(
//            summary = "Create a new book",
//            description = "This API allows you to upload a book with audio and PDF files",
//            responses = {
//                    @ApiResponse(responseCode = "201", description = "Book created"),
//                    @ApiResponse(responseCode = "400", description = "Invalid file type or request error"),
//                    @ApiResponse(responseCode = "404", description = "Book not found")
//            }
//    )
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    @Transactional
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

            Book book = bookService.saveBook(bookName, authorName, type, bookCategoryId, audioFile, pdfBookFile);
            return new ResponseEntity<>("book " + book.getId() + " shu id bilan saqlandi!", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
