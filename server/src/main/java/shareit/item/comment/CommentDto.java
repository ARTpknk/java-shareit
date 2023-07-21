package shareit.item.comment;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    @With
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
