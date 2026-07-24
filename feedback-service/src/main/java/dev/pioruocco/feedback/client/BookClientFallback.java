package dev.pioruocco.feedback.client;

import org.springframework.stereotype.Component;

@Component
public class BookClientFallback implements BookClient {

    @Override
    public BookResponse findById(Integer bookId) {
        throw new BookServiceUnavailableException(
                "Book service is currently unavailable, cannot validate the book for this feedback");
    }
}
