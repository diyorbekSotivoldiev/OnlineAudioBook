package org.example.onlineaudiobook.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.Audio;
import org.example.onlineaudiobook.repository.AudioRepository;
import org.example.onlineaudiobook.service.AudioService;
import org.example.onlineaudiobook.service.BookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api1/audio")
@RequiredArgsConstructor
public class AudioController {
    @Value("${server.url}")
    String serverUrl;
    @Value("${file.basePath}")
    String fileBasePath;

    private final AudioService audioService;
    private final BookService bookService;
    private final AudioRepository audioRepository;

    @GetMapping("findByFileName/{fileName}")
    public HttpEntity<?> getAudioByFileName(@PathVariable String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg"); // Audio fayl turi, masalan MP3 uchun
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audio.mp3\""); // Faylni yuklab olishda taklif qilinadigan nom
        return new ResponseEntity<>(audioService.getAudio(fileName), headers, HttpStatus.OK);
    }

    /*@PostMapping
    public HttpEntity<?> save(@RequestBody MultipartFile file) {
        BookService.Result result = bookService.saveFile(file, "audioFile", serverUrl + "api/audio/findByFileName/");
        Audio audio = audioRepository.save(Audio.builder().name(file.getOriginalFilename()).url(result.objUrl()).fileName(result.fileName()).build());
        return ResponseEntity.ok(audio);
    }*/


}