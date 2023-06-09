package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "text", length=512, nullable=false)
    private String text;
    @Column(name = "item_id")
    private Integer itemId;
    @Column(name = "author_id")
    private Integer authorId;
    @Column(name = "comment_created")
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
