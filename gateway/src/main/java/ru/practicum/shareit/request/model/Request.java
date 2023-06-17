package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class Request {

    private int id;
    private String description;
    private User requestor;
    private Integer requestorId;
    private LocalDateTime created;

    public Request() {
    }

    public Request(int id, String description, int requestorId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
    }

    public Request(int id, String description, User requestor, Integer requestorId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.requestorId = requestorId;
        this.created = created;
    }
}
