package com.edition.service;

import com.edition.model.Book;
import com.edition.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        // Vérifier unicité ISBN
        if (book.getIsbn() != null && bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("ISBN déjà utilisé");
        }

        // Vérifier année publication
        int currentYear = Year.now().getValue();
        if (book.getPublicationYear() != null && book.getPublicationYear() > currentYear) {
            throw new IllegalArgumentException("L'année de publication ne peut pas être dans le futur");
        }

        return bookRepository.save(book);
    }

    public Optional<Book> update(Long id, Book bookDetails) {
        return bookRepository.findById(id).map(existingBook -> {

            if (bookDetails.getTitle() != null && !bookDetails.getTitle().isBlank()) {
                existingBook.setTitle(bookDetails.getTitle());
            }

            if (bookDetails.getAuthor() != null && !bookDetails.getAuthor().isBlank()) {
                existingBook.setAuthor(bookDetails.getAuthor());
            }

            // ISBN : si changé, vérifier unicité
            if (bookDetails.getIsbn() != null && !bookDetails.getIsbn().isBlank()) {
                if (!bookDetails.getIsbn().equals(existingBook.getIsbn())
                        && bookRepository.existsByIsbn(bookDetails.getIsbn())) {
                    throw new IllegalArgumentException("ISBN déjà utilisé");
                }
                existingBook.setIsbn(bookDetails.getIsbn());
            }

            if (bookDetails.getPrice() != null) {
                existingBook.setPrice(bookDetails.getPrice());
            }

            if (bookDetails.getDescription() != null) {
                existingBook.setDescription(bookDetails.getDescription());
            }

            if (bookDetails.getCoverUrl() != null) {
                existingBook.setCoverUrl(bookDetails.getCoverUrl());
            }

            if (bookDetails.getPublicationYear() != null) {
                int currentYear = Year.now().getValue();
                if (bookDetails.getPublicationYear() > currentYear) {
                    throw new IllegalArgumentException("Année de publication invalide");
                }
                existingBook.setPublicationYear(bookDetails.getPublicationYear());
            }

            if (bookDetails.getCategory() != null) {
                existingBook.setCategory(bookDetails.getCategory());
            }

            return bookRepository.save(existingBook);
        });
    }

    public boolean delete(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
