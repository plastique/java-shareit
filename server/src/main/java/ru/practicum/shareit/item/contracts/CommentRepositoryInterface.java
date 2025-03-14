package ru.practicum.shareit.item.contracts;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepositoryInterface extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItem_Id(Long id, Sort sort);

    @EntityGraph(Comment.ENTITY_GRAPH_COMMENT_ITEM)
    List<Comment> findAllByItem_IdIn(List<Long> itemIds, Sort sort);

}
