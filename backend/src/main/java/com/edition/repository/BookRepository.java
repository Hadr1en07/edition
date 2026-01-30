package com.edition.repository;
import com.edition.model.Book;
import com.edition.model.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategory(BookCategory category);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByTitleContainingIgnoreCase(String title);
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
}
