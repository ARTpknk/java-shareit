package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    @With
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private Integer itemId;
    private User booker;
    private BookingStatus status;
}
