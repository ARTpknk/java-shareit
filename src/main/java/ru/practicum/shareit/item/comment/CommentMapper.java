package ru.practicum.shareit.item.comment;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment, String authorName) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(authorName)
                .created(comment.getCreated())
                .build();
    }

    public Comment toComment(CommentDto commentDto, int itemId, int authorId) {
        return Comment.builder()
                .id(itemId)
                .text(commentDto.getText())
                .authorId(authorId)
                .created(commentDto.getCreated())
                .build();
    }
}
