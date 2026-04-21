package dev.pioruocco.book.history;

import dev.pioruocco.book.book.Book;
import dev.pioruocco.book.common.BaseEntity;
import dev.pioruocco.book.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookTransactionHistory extends BaseEntity {

    // @ManyToOne
    @Column(name = "user_id")
    private String userId;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private boolean returned;
    private boolean returnApproved;
}
