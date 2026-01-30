package com.edition.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 2, max = 200)
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "L'auteur est obligatoire")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String author;

    @NotBlank(message = "L'ISBN est obligatoire")
    @Pattern(regexp = "^978-\\d{10}$", message = "Format ISBN invalide (978-XXXXXXXXXX)")
    @Column(nullable = false, unique = true, length = 14)
    private String isbn;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    @Column(nullable = false)
    private Double price;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotNull(message = "La catégorie est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookCategory category;

    @NotNull(message = "L'année de publication est obligatoire")
    @Min(value = 1800, message = "Année min: 1800")
    @Column(nullable = false)
    private Integer publicationYear;

    @Column
    private String coverUrl;
}
