package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.classes.Create;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.user.dto.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    @With
    int id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    Integer itemId;
    User booker;
    BookingStatus status;
}
