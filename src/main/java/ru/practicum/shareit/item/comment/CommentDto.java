package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    @With
    private int id;
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;
}
