package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.classes.Create;
import ru.practicum.shareit.item.comment.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ItemWithBookingsDto {
    @With
    private int id;
    @NotBlank(groups = Create.class)
    private String name;
    @NotBlank(groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentDto> comments;
}
