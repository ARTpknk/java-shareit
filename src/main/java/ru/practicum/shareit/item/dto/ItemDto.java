package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {
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