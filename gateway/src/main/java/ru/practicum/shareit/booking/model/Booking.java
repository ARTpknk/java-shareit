package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class Booking {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer itemId;
    private Item item;
    private Integer bookerId;
    private User booker;
    private BookingStatus status;

    public Booking() {
    }

    public Booking(int id, LocalDateTime start, LocalDateTime end, Integer itemId, Integer bookerId,
                   BookingStatus status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.status = status;
    }

    public Booking(int id, LocalDateTime start, LocalDateTime end, Integer itemId, Item item,
                   Integer bookerId, User booker, BookingStatus status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.item = item;
        this.bookerId = bookerId;
        this.booker = booker;
        this.status = status;
    }
}
