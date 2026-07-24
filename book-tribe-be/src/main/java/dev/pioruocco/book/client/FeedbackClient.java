package dev.pioruocco.book.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "feedback-service", fallback = FeedbackClientFallback.class)
public interface FeedbackClient {

    @GetMapping("/api/v1/feedbacks/book/{book-id}/average-rating")
    Double findAverageRatingByBook(@PathVariable("book-id") Integer bookId);
}
