package ru.practicum.shareit.item.comment;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    int id;
    @Column(name = "text", length=512, nullable=false)
    String text;
    @Column(name = "item_id")
    Integer itemId;
    @Column(name = "author_id")
    Integer authorId;
    @Column(name = "comment_created")
    LocalDateTime created;

    public Comment() {
    }

    public Comment(int id, String text, Integer itemId, Integer authorId) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.authorId = authorId;
    }
}
