package com.edition.config;

import com.edition.model.Book;
import com.edition.model.BookCategory;
import com.edition.model.User;
import com.edition.repository.BookRepository;
import com.edition.repository.UserRepository;
import com.edition.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // === USERS ===
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);
            System.out.println(">>> Utilisateur admin créé: admin / admin123");
        }

        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);
            System.out.println(">>> Utilisateur user créé: user / user123");
        }

        // === BOOKS ===
        if (bookRepository.count() == 0) {
            List<Book> demo = List.of(
                    Book.builder()
                            .title("Les Misérables")
                            .author("Victor Hugo")
                            .isbn("978-0000000001")
                            .price(14.90)
                            .category(BookCategory.ROMAN)
                            .publicationYear(1862)
                            .description("Un grand classique de la littérature française.")
                            .coverUrl("https://covers.openlibrary.org/b/id/8231856-L.jpg")
                            .build(),
                    Book.builder()
                            .title("Les Fleurs du mal")
                            .author("Charles Baudelaire")
                            .isbn("978-0000000002")
                            .price(10.50)
                            .category(BookCategory.POESIE)
                            .publicationYear(1857)
                            .description("Recueil poétique majeur du XIXe siècle.")
                            .coverUrl("https://covers.openlibrary.org/b/id/10523344-L.jpg")
                            .build(),
                    Book.builder()
                            .title("Phèdre")
                            .author("Jean Racine")
                            .isbn("978-0000000003")
                            .price(8.90)
                            .category(BookCategory.THEATRE)
                            .publicationYear(1677)
                            .description("Tragédie classique en cinq actes.")
                            .coverUrl("https://covers.openlibrary.org/b/id/10510734-L.jpg")
                            .build(),
                    Book.builder()
                            .title("Du contrat social")
                            .author("Jean-Jacques Rousseau")
                            .isbn("978-0000000004")
                            .price(9.90)
                            .category(BookCategory.ESSAI)
                            .publicationYear(1762)
                            .description("Essai politique fondateur.")
                            .coverUrl("https://covers.openlibrary.org/b/id/11153206-L.jpg")
                            .build(),
                    Book.builder()
                            .title("Mémoires d'outre-tombe")
                            .author("Chateaubriand")
                            .isbn("978-0000000005")
                            .price(16.00)
                            .category(BookCategory.BIOGRAPHIE)
                            .publicationYear(1848)
                            .description("Autobiographie monumentale.")
                            .coverUrl("https://covers.openlibrary.org/b/id/12015514-L.jpg")
                            .build(),
                    Book.builder()
                            .title("Germinal")
                            .author("Émile Zola")
                            .isbn("978-0000000006")
                            .price(12.80)
                            .category(BookCategory.ROMAN)
                            .publicationYear(1885)
                            .description("Roman social sur le monde ouvrier.")
                            .coverUrl("https://covers.openlibrary.org/b/id/8231857-L.jpg")
                            .build(),
                    Book.builder()
                            .title("Cyrano de Bergerac")
                            .author("Edmond Rostand")
                            .isbn("978-0000000007")
                            .price(11.20)
                            .category(BookCategory.THEATRE)
                            .publicationYear(1897)
                            .description("Comédie héroïque en vers.")
                            .coverUrl("https://covers.openlibrary.org/b/id/10510745-L.jpg")
                            .build(),
                    Book.builder()
                            .title("Essais")
                            .author("Michel de Montaigne")
                            .isbn("978-0000000008")
                            .price(15.40)
                            .category(BookCategory.ESSAI)
                            .publicationYear(1580)
                            .description("Réflexions humanistes, personnelles et philosophiques.")
                            .coverUrl("https://covers.openlibrary.org/b/id/11153210-L.jpg")
                            .build()
            );

            bookRepository.saveAll(demo);
            System.out.println(">>> 8 livres de démonstration créés");
        }
    }
}

