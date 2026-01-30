package com.edition.controller;

import com.edition.model.Book;
import com.edition.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // GET /api/books (public)
    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    // GET /api/books/{id} (public)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return bookService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Livre non trouvé")));
    }

    // POST /api/books (admin)
    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
        return ResponseEntity.ok(bookService.save(book));
    }

    // PUT /api/books/{id} (admin)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        return bookService.update(id, bookDetails)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Livre non trouvé")));
    }

    // DELETE /api/books/{id} (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = bookService.delete(id);
        if (!deleted) {
            return ResponseEntity.status(404).body(Map.of("error", "Livre non trouvé"));
        }
        return ResponseEntity.ok(Map.of("message", "Livre supprimé"));
    }
}
