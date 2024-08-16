package org.example.onlineaudiobook.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.repository.BookRepository;
import org.example.onlineaudiobook.service.MarkService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mark")
@RequiredArgsConstructor
public class MarkController {
    private final MarkService markService;
    private final BookRepository bookRepository;

    @PostMapping
    public HttpEntity<?> markUser(@RequestParam("userId") Long userId, @RequestParam("bookId") Long bookId, @RequestParam("mark") Integer mark) {
        if (mark < 1 || mark > 5)
            throw new RuntimeException("bookga bunday baho berib bo'lmaydi, 1 <= baho <= 5 gacha baholang");
        if (bookRepository.existsById(bookId)) {
            if (markService.isAlreadyMarked(userId, bookId)) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("bu avval baho bergan ekanku...");
            } else {
                markService.saveNewMark(userId, mark, bookId);
                return ResponseEntity.ok(mark + " ushbu baho muvaffaqqiyatli kiritildi");
            }
        }
        throw new RuntimeException("book topilmadi");
    }
}
