package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
public class Item {
    int id;
    @NonNull
    @NotBlank
    String name;
    @NonNull
    @NotBlank
    String description;
    @NonNull
    Boolean available; //NonNull не работает с примитивами
    User owner;
    ItemRequest request;
}
