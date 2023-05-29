package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.user.dto.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
public class Booking {
    @Id
    int id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    User booker;
    @Enumerated(EnumType.STRING)
    BookingStatus status;
}
