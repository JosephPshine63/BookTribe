package dev.pioruocco.feedback.client;

public class BookServiceUnavailableException extends RuntimeException {

    public BookServiceUnavailableException(String message) {
        super(message);
    }
}
