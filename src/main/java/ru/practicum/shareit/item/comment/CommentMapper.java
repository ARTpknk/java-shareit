package ru.practicum.shareit.item.comment;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment, String authorName){
        return new CommentDto(comment.getId(), comment.getText(), authorName, comment.getCreated());
    }

    public Comment toComment (CommentDto commentDto, int itemId, int authorId){
        return new Comment(commentDto.getId(), commentDto.getText(), itemId, authorId);
    }
}
