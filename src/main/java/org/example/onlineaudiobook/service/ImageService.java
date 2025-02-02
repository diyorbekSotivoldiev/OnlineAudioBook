package org.example.onlineaudiobook.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.Image;
import org.example.onlineaudiobook.repository.ImageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public Image saveImage(byte[] firstPageOfPdf) {
        return imageRepository.save(new Image(null, firstPageOfPdf));
    }

    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow();
    }
}
