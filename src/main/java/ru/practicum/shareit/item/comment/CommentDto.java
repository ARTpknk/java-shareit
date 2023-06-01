package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    @With
    int id;
    @NotBlank
    String text;
    String authorName;
    LocalDateTime created;
}
