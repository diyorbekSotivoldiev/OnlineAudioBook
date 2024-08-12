package org.example.onlineaudiobook.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.Book;
import org.example.onlineaudiobook.responseDto.BookResponseDTO;
import org.example.onlineaudiobook.service.BookService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

//    @GetMapping("/{id}/first-page")
//    public HttpEntity<?> getFirstPage(@PathVariable int id) {
//        byte[] firstPageBytes = bookService.getFirstPageAsByteArray(id);
//
//        if (firstPageBytes != null) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Content-Type", "image/png");
//            return new ResponseEntity<>(firstPageBytes, headers, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/convert/{bookId}")
    public String convertPdfToImage(@PathVariable Long bookId) {
        try {
            return bookService.convertFirstPageToImage(bookId);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error converting PDF to image";
        }
    }

    @GetMapping
    public HttpEntity<?> getAllBooks() {
        List<BookResponseDTO> allBooks = bookService.getAllBooks();

        return ResponseEntity.ok(allBooks);
    }

    @Transactional
    @PostMapping("/create")
    public HttpEntity<?> createBook(
            @RequestParam("bookName") String bookName,
            @RequestParam("authorName") String authorName,
            @RequestParam("type") String type,
            @RequestParam("bookCategoryId") Long bookCategoryId,
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam("pdfBookFile") MultipartFile pdfBookFile) {
        try {
            if (!Objects.equals(audioFile.getContentType(), "audio/mpeg")) {
                return new ResponseEntity<>("Invalid audio file type. Only MP3 is allowed.", HttpStatus.BAD_REQUEST);
            }
            if (!Objects.equals(pdfBookFile.getContentType(), "application/pdf")) {
                return new ResponseEntity<>("Invalid PDF file type. Only PDF is allowed.", HttpStatus.BAD_REQUEST);
            }

            byte[] audioData = audioFile.getBytes();
            byte[] pdfData = pdfBookFile.getBytes();
            System.out.println("audio: " + Arrays.toString(audioData));
            System.out.println("pdf:  " + Arrays.toString(pdfData));

            Book book = bookService.saveBook(bookName, authorName, type, bookCategoryId, audioData, pdfData);
            return new ResponseEntity<>("book", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{bookId}")
    public HttpEntity<?> getPdfFile(@PathVariable Long bookId) {
        try {
            byte[] pdfData = bookService.getPdfOfBook(bookId);
            if (pdfData.length==0){
                throw new RuntimeException("nullku");
            }
            ByteArrayResource resource = new ByteArrayResource(pdfData);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; pdfOfBook=\"book.pdf\"");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/play/{bookId}")
    public ResponseEntity<byte[]> getAudio(@PathVariable Long bookId) {
        byte[] audioBytes = bookService.getAudioByBookId(bookId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(audioBytes.length);
        headers.set("Content-Disposition", "inline; aaa=\"audio.mp3\"");

        return new ResponseEntity<>(audioBytes, headers, HttpStatus.OK);
    }
}
