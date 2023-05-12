package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.practicum.shareit.classes.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {
    @With
    private int id;
    @NotBlank(groups = Create.class)
    private String name;
    @NotBlank(groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
}