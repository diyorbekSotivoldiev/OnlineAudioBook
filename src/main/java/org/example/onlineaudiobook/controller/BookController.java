package org.example.onlineaudiobook.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.Book;
import org.example.onlineaudiobook.requestDto.BookSaveRequest;
import org.example.onlineaudiobook.responseDto.BookResponseDTO;
import org.example.onlineaudiobook.service.BookService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    @GetMapping("/{id}/first-page")
    public HttpEntity<?> getFirstPage(@PathVariable int id) {
        byte[] firstPageBytes = bookService.getFirstPageAsByteArray(id);

        if (firstPageBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");
            return new ResponseEntity<>(firstPageBytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping
    public HttpEntity<?> getAllBooks(){
        List<BookResponseDTO> allBooks = bookService.getAllBooks();

        return ResponseEntity.ok(allBooks);
    }
        @PostMapping("/create")
        public HttpEntity<?> createBook(@RequestBody BookSaveRequest bookSaveRequest,
                                             @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
                                             @RequestPart(value = "pdfBookFile", required = false) MultipartFile pdfBookFile) {
            System.out.println(bookSaveRequest);
            System.out.println(audioFile);
            System.out.println(pdfBookFile);
            try {
                Book book = bookService.saveBook(bookSaveRequest, audioFile, pdfBookFile);
                return new ResponseEntity<>(book, HttpStatus.CREATED);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
}
