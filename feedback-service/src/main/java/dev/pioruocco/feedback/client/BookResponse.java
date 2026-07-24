package dev.pioruocco.feedback.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Mirrors the subset of book-service's BookResponse fields feedback-service
 * needs to validate a feedback (ownership, archived/shareable state).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String createdBy;
    private byte[] cover;
    private double rate;
    private boolean archived;
    private boolean shareable;
}
