package org.example.onlineaudiobook.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.*;
import org.example.onlineaudiobook.entity.enums.BookType;
import org.example.onlineaudiobook.projection.BookProjection;
import org.example.onlineaudiobook.repository.*;
import org.example.onlineaudiobook.requestDto.BookSaveRequest;
import org.example.onlineaudiobook.responseDto.BookResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    private final AudioRepository audioRepository;
    private final PdfBookRepository pdfBookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final PdfBookService pdfBookService;
    private final ImageService imageService;
    @Value("${server.url}")
    String serverUrl;
    @Value("${file.basePath}")
    String fileBasePath;


    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAllBooks() {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        List<Book> books = bookRepository.findAll(sort);
        return books.stream().map(this::convertToDto).toList();
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
                .mapToLong(rating -> rating.getMarkUsers().size())
                .sum();

        return (float) 1.0 * totalMarks / count;
    }

    private BookResponseDTO convertToDto(Book book) {
        return BookResponseDTO
                .builder()
                .id(book.getId())
                .name(book.getBookName())
                .author(book.getAuthorName())
                .bookType(book.getType())
                .category(book.getBookCategory())
                .imageUrl(serverUrl + "api/book/image/" + (book.getImage()==null?"0":book.getImage().getId()))
                .pdfUrl(book.getPdfBook()==null?"":book.getPdfBook().getUrl())
                .audioUrl(book.getAudio()==null?"":book.getAudio().getUrl())
                .mark(calculateAverageMark(book))
                .build();
    }

    @Transactional
    public Book saveBook(String bookName, String authorName, BookType type, Long bookCategoryId, @NotNull MultipartFile audioData, @NotNull MultipartFile pdfData) throws IOException {
        BookSaveRequest bookSaveRequest = new BookSaveRequest(bookName, authorName, type, bookCategoryId);
        BookCategory bookCategory = bookCategoryRepository.findById(bookSaveRequest.getBookCategoryId()).orElseThrow(() -> new RuntimeException("BookCategory not found"));
        PdfBook pdfBook = null;
        if (pdfData != null && pdfData.getInputStream().readAllBytes().length != 0) {
            Result result = saveFile(pdfData, "pdfBookFile", serverUrl + "api/pdfBook/findByFileName/");
            pdfBook = PdfBook
                    .builder()
                    .fileName(result.fileName)
                    .url(result.objUrl)
                    .build();
            pdfBookRepository.save(pdfBook);
        }
        Audio audio = null;
        if (audioData != null && audioData.getInputStream().readAllBytes().length != 0) {
            BookService.Result result = saveFile(audioData, "audioFile", serverUrl + "api/audio/findByFileName/");
            audio = Audio
                    .builder()
                    .name(bookSaveRequest.getBookName())
                    .fileName(result.fileName())
                    .url(result.objUrl())
                    .build();
            audioRepository.save(audio);
        }


        byte[] firstPageOfPdf = pdfBookService.getFirstPageOfPdf(pdfData.getBytes());
        Image image = null;
        if (firstPageOfPdf != null && firstPageOfPdf.length != 0) {
            image = imageService.saveImage(firstPageOfPdf);
        }
        Book book = Book.builder()
                .bookName(bookSaveRequest.getBookName())
                .authorName(bookSaveRequest.getAuthorName())
                .type(type)
                .bookCategory(bookCategory)
                .image(image)
                .audio(audio)
                .pdfBook(pdfBook)
                .createdAt(LocalDateTime.now())
                .build();
        return bookRepository.save(book);
    }

    @Transactional
    public Result saveFile(MultipartFile audioData, String directoryName, String api) {
        Path path = Paths.get(fileBasePath + directoryName);
        try {
            if (!Files.exists(path)) {
                Path directories;
                directories = Files.createDirectories(path);
                System.out.println("creted now direct: " + directories);
            }
            String fileName = UUID.randomUUID() + getFileExtension(audioData);
            InputStream inputStream = audioData.getInputStream();
            byte[] bytes = inputStream.readAllBytes();
            Path path1 = Paths.get(fileBasePath + directoryName + "/" + fileName);
            Files.write(path1, bytes);
            String objUrl = api + fileName;
            return new Result(fileName, objUrl);
        } catch (IOException e) {
            throw new RuntimeException("directory yaratilmadi yoki byte[] ga o'girilmadi");
        }
    }

    public String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        } else {
            throw new RuntimeException("invalid file");
        }
    }

    public List<BookResponseDTO> getByCategoryId(Long categoryId) {
        List<Book> allBooks = bookRepository.findAllByBookCategoryIdOrderByCreatedAt(categoryId);
        return allBooks.stream().map(this::convertToDto).toList();
    }

    public BookResponseDTO getById(Long id) {
        return convertToDto(bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found")));
    }

    public List<BookProjection> searchBook(String text) {
        return bookRepository.searchBook(text);
    }

    public record Result(String fileName, String objUrl) {
    }

    public PdfBook getPdfOfBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("pdf of book not found")).getPdfBook();
    }

    public Audio getAudioByBookId(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("audio of book not found")).getAudio();
    }
}

