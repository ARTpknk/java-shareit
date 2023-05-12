package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.dto.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    int id;
    String description;
    User requestor;
    LocalDateTime created;
}
