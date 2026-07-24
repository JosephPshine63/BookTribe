package dev.pioruocco.feedback.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", fallback = BookClientFallback.class)
public interface BookClient {

    @GetMapping("/api/v1/books/{book-id}")
    BookResponse findById(@PathVariable("book-id") Integer bookId);
}
