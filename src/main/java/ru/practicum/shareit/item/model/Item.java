package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class Item {
    @With
    int id;
    @NotNull
    @NotBlank
    String name;
    @NotNull
    @NotBlank
    String description;
    @NotNull
    Boolean available; //NonNull не работает с примитивами
    int owner;
    ItemRequest request;
}
