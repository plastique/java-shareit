package ru.practicum.shareit.item.contracts;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

public interface CommentRepositoryInterface extends JpaRepository<Comment, Long> {

}
