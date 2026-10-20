package dev.pioruocco.feedback.seed;

import dev.pioruocco.feedback.feedback.FeedBackRepository;
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
 * Loads demo feedbacks from seed/feedbacks.sql when application.seed.enabled is true
 * and the feedback table is still empty. Book ids match the ones in book-service's seed/books.sql.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "application.seed.enabled", havingValue = "true")
public class DataSeeder implements ApplicationRunner {

    private final FeedBackRepository feedBackRepository;
    private final DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) {
        if (feedBackRepository.count() > 0) {
            log.info("Feedbacks already present, skipping seed");
            return;
        }
        new ResourceDatabasePopulator(new ClassPathResource("seed/feedbacks.sql")).execute(dataSource);
        log.info("Seeded {} demo feedbacks", feedBackRepository.count());
    }
}
