package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "request_description", length = 512, nullable = false)
    private String description;
    @Transient
    private User requestor;
    @Column(name = "requestor_id", nullable = false)
    private Integer requestorId;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    public Request() {
    }

    public Request(int id, String description, int requestorId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
    }
}
