package org.example.onlineaudiobook.component;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.*;
import org.example.onlineaudiobook.entity.enums.BookType;
import org.example.onlineaudiobook.entity.enums.Role;
import org.example.onlineaudiobook.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final MarkUserRepository markUserRepository;
    private final RatingRepository ratingRepository;

    @Override
    public void run(String... args) {
        if (ddl.equals("create")) {

            String[] bookCategoryArray = new String[]{
                    "BUSINESS", "PERSONALIZE", "MUSIC", "PHOTOGRAPHY"
            };
            List<Book> books = new ArrayList<>();
            for (int i = 0; i < bookCategoryArray.length; i++) {
                BookCategory save = bookCategoryRepository.save(BookCategory.builder().name(bookCategoryArray[i]).build());
                for (int j = 0; j < 10; j++) {
                    books.add(bookRepository.save(Book.builder()
                            .bookCategory(save)
                            .bookName("book " + (j + i))
                            .authorName("author " + (i + j))
                            .type(BookType.values()[(j + i) % 10])
                            .build()));
                }
            }
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                users.add(userRepository.save(User.builder()
                        .username("username%s".formatted(i))
                        .phone("phone" + i)
                        .email("user%s@gmail.com".formatted(i))
                        .displayName("John Doe%s".formatted(i))
                        .password(passwordEncoder.encode("123"))
                        .birthDate(LocalDate.now())
                        .active(true)
                        .role(i % 2 == 0 ? Role.USER : Role.ADMIN)
                        .build()));
            }
            Random random = new Random();
            for (Book book : books) {
                if (book.getId() % 3 == 0) {
                    Rating rating = ratingRepository.save(new Rating(null, new ArrayList<>(), book));
                    for (User user : users) {
                        MarkUser markUser = markUserRepository.save(new MarkUser(null, user, random.nextInt(1, 5)));
                        rating.getMarkUsers().add(markUser);
                    }
                    ratingRepository.save(rating);
                }
            }
        }
    }
}
