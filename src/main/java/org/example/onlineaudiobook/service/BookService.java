package org.example.onlineaudiobook.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.example.onlineaudiobook.entity.*;
import org.example.onlineaudiobook.entity.enums.BookType;
import org.example.onlineaudiobook.repository.*;
import org.example.onlineaudiobook.requestDto.BookSaveRequest;
import org.example.onlineaudiobook.responseDto.BookResponseDTO;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    private final AudioRepository audioRepository;
    private final PdfBookRepository pdfBookRepository;
    private final BookCategoryRepository bookCategoryRepository;

    public byte[] getFirstPageAsByteArray(long pdfBookId) {
        Optional<Book> bookOptional = bookRepository.findById(pdfBookId);

        if (bookOptional.isPresent()) {
            byte[] pdfBytes = bookOptional.get().getPdfBook().getContent();

            try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
                if (document.getNumberOfPages() > 0) {
                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                    BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                    return byteArrayOutputStream.toByteArray();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAllBooks() {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        List<Book> books = bookRepository.findAll(sort);
        return books.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private float calculateAverageMark(Book book) {
        List<Rating> ratings = ratingRepository.findByBook(book);
        if (ratings.isEmpty()) {
            return 0.0f;
        }

        int totalMarks = ratings.stream()
                .flatMap(rating -> rating.getMarkUsers().stream())
                .mapToInt(MarkUser::getMark)
                .sum();

        int count = (int) ratings.stream()
                .flatMap(rating -> rating.getMarkUsers().stream())
                .count();

        return (float) 1.0 * totalMarks / count;
    }

    private BookResponseDTO convertToDto(Book book) {
        return BookResponseDTO
                .builder()
                .name(book.getBookName())
                .author(book.getAuthorName())
                .bookType(book.getType())
                .category(book.getBookCategory())
                .image(book.getImage() != null ? book.getImage().getContent() : null)
                .mark(calculateAverageMark(book))
                .build();
    }

    public Book saveBook(BookSaveRequest bookSaveRequest, MultipartFile audioFile, MultipartFile pdfBookFile) throws IOException {
        BookCategory bookCategory = bookCategoryRepository.findById(bookSaveRequest.getBookCategoryId()).orElseThrow(() -> new RuntimeException("BookCategory not found"));

        Audio audio = null;
        if (audioFile != null && !audioFile.isEmpty()) {
            byte[] audioData = audioFile.getBytes();
            audio = Audio
                    .builder()
                    .name(bookSaveRequest.getBookName())
                    .content(audioData)
                    .build();
            audioRepository.save(audio);
        }

        PdfBook pdfBook = null;
        if (pdfBookFile != null && !pdfBookFile.isEmpty()) {
            byte[] pdfBookData = pdfBookFile.getBytes();
            pdfBook = PdfBook
                    .builder()
                    .content(pdfBookData)
                    .build();
            pdfBookRepository.save(pdfBook);
        }

        Book book = Book.builder()
                .bookName(bookSaveRequest.getBookName())
                .authorName(bookSaveRequest.getAuthorName())
                .type(BookType.valueOf(bookSaveRequest.getType()))
                .bookCategory(bookCategory)
                .audio(audio)
                .pdfBook(pdfBook)
                .createdAt(LocalDateTime.now())
                .build();

        return bookRepository.save(book);
    }
    }

