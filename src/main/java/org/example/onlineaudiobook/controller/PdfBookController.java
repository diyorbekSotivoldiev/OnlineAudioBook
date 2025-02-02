package org.example.onlineaudiobook.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.PdfBook;
import org.example.onlineaudiobook.repository.PdfBookRepository;
import org.example.onlineaudiobook.service.BookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api1/pdfBook")
@RequiredArgsConstructor
public class PdfBookController {
    private final BookService bookService;
    @Value("${file.basePath}")
    String fileBasePath;
    @Value("${server.url}")
    String serverUrl;
    //private final PdfBookRepository pdfBookRepository;


    @GetMapping("findByFileName/{fileName}")
    public HttpEntity<?> getFile(@PathVariable String fileName) {
        Path path = Paths.get(fileBasePath + "pdfBookFile/" + fileName);
        if (!Files.exists(path)) {
            throw new RuntimeException("pdf file not found with the name: " + fileName);
        }
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"book.pdf\"");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

//    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public HttpEntity<?> save(@RequestBody MultipartFile file) {
//        BookService.Result result = bookService.saveFile(file, "pdfBookFile", serverUrl + "api/pdfBook/findByFileName/");
//        PdfBook pdfBook = pdfBookRepository.save(PdfBook.builder().url(result.objUrl()).fileName(result.fileName()).build());
//        return ResponseEntity.ok(pdfBook);
//    }
}
