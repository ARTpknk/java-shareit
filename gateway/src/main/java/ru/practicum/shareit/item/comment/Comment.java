package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data

@Builder
public class Comment {


    private int id;
    private String text;
    private Integer itemId;
    private Integer authorId;
    private LocalDateTime created;

    public Comment() {
    }

    public Comment(int id, String text, Integer itemId, Integer authorId) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.authorId = authorId;
    }

    public Comment(int id, String text, Integer itemId, Integer authorId, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.authorId = authorId;
        this.created = created;
    }
}
