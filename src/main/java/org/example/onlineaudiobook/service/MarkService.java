package org.example.onlineaudiobook.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineaudiobook.entity.Book;
import org.example.onlineaudiobook.entity.MarkUser;
import org.example.onlineaudiobook.entity.Rating;
import org.example.onlineaudiobook.entity.User;
import org.example.onlineaudiobook.repository.BookRepository;
import org.example.onlineaudiobook.repository.MarkUserRepository;
import org.example.onlineaudiobook.repository.RatingRepository;
import org.example.onlineaudiobook.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MarkService {
    private final MarkUserRepository markUserRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public boolean isAlreadyMarked(Long userId, Long bookId) {
        Rating rating = ratingRepository.findByBook_Id(bookId).orElse(null);
        if (rating != null) {
            return rating.getMarkUsers().stream().anyMatch(item -> item.getUser().getId().equals(userId));
        }
        return false;
    }

    public void saveNewMark(Long userId, Integer mark, Long bookId) {
        Rating rating = ratingRepository.findByBook_Id(bookId).orElse(null);
        Book book = bookRepository.findById(bookId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        if (rating != null) {
            Rating rating1 = ratingRepository.findByBook_Id(bookId).orElseThrow();
            MarkUser markUser = markUserRepository.save(new MarkUser(null, user, mark));
            rating1.getMarkUsers().add(markUser);
            ratingRepository.save(rating1);
        } else {
            Rating rating1 = ratingRepository.save(new Rating(null, new ArrayList<>(), book));
            MarkUser markUser = markUserRepository.save(new MarkUser(null, user, mark));
            rating1.getMarkUsers().add(markUser);
            ratingRepository.save(rating1);
        }
    }
}
