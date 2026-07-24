package dev.pioruocco.feedback.feedback;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {

    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .bookId(request.bookId())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, String currentUser) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), currentUser))
                .build();
    }
}
