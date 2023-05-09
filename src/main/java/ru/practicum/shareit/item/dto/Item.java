package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
public class Item {
    @With
    int id;
    String name;
    String description;
    Boolean available;
    int owner;
}
