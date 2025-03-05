package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    private CommentMapper() {
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText()
        );
    }

    public static Comment toComment(CommentDto dto) {
        return new Comment(
                null,
                dto.getText(),
                null,
                null
        );
    }

}
