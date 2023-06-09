package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;
    @Column(name = "item_id", nullable = false)
    private Integer itemId;
    @Transient
    private Item item;
    @Column(name = "booker_id", nullable = false)
    private Integer bookerId;
    @Transient
    private User booker;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
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
