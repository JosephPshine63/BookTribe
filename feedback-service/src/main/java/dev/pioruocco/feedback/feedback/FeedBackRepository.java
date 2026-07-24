package dev.pioruocco.feedback.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<Feedback, Integer> {

    Page<Feedback> findAllByBookId(Integer bookId, Pageable pageable);

    List<Feedback> findAllByBookId(Integer bookId);
}
