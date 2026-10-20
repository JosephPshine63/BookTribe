package dev.pioruocco.book.seed;

import dev.pioruocco.book.book.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Loads demo books and borrow history from seed/books.sql when application.seed.enabled is true
 * and the book table is still empty. Owners and borrowers are the user ids of the realm export.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "application.seed.enabled", havingValue = "true")
public class DataSeeder implements ApplicationRunner {

    private final BookRepository bookRepository;
    private final DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) {
        if (bookRepository.count() > 0) {
            log.info("Books already present, skipping seed");
            return;
        }
        new ResourceDatabasePopulator(new ClassPathResource("seed/books.sql")).execute(dataSource);
        log.info("Seeded {} demo books", bookRepository.count());
    }
}
