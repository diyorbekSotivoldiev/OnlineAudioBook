package org.example.onlineaudiobook.service;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class AudioService {
    @Value("${server.url}")
    String serverUrl;
    @Value("${file.basePath}")
    String fileBasePath;

    public byte[] getAudio(String fileName) {
        Path path = Paths.get(fileBasePath + "audioFile/" + fileName);
        if (!Files.exists(path)) {
            throw new RuntimeException("file not found with name: " + fileName);
        }
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

