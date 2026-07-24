package dev.pioruocco.book.client;

import org.springframework.stereotype.Component;

@Component
public class FeedbackClientFallback implements FeedbackClient {

    @Override
    public Double findAverageRatingByBook(Integer bookId) {
        return 0.0;
    }
}
