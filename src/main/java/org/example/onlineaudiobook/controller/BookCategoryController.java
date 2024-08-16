package org.example.onlineaudiobook.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.repository.BookCategoryRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookCategory")
public class BookCategoryController {
    private final BookCategoryRepository bookCategoryRepository;

    @GetMapping("/getAll")
    public HttpEntity<?> getCategories() {
        return ResponseEntity.ok(bookCategoryRepository.findAll());
    }

    @GetMapping("categoryId/{categoryId}")
    public HttpEntity<?> getOne(@PathVariable Long categoryId) {
        return ResponseEntity.ok(bookCategoryRepository.findById(categoryId));
    }
}
