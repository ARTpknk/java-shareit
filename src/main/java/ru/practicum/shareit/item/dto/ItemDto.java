package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {
    int id;
    @NonNull
    @NotBlank
    String name;
    @NonNull
    @NotBlank
    String description;
    @NonNull
    Boolean available;
    User owner;
    ItemRequest request;


}
