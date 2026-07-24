package dev.pioruocco.book.book;

import dev.pioruocco.book.client.FeedbackClient;
import dev.pioruocco.book.file.FileUtils;
import dev.pioruocco.book.history.BookTransactionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMapper {

    private final FeedbackClient feedbackClient;

    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .isbn(request.isbn())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(feedbackClient.findAverageRatingByBook(book.getId()))
                .archived(book.isArchived())
                .shareable(book.isShareable())
                // .owner(book.getOwner().fullName())
                .createdBy(book.getCreatedBy())
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(feedbackClient.findAverageRatingByBook(history.getBook().getId()))
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
