package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", nullable = false)
    int id;
    @Column(name = "start_date", nullable = false)
    LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    LocalDateTime end;
    @Column(name = "item_id", nullable = false)
    Integer itemId;
    @Transient
    Item item;
    @Column(name = "booker_id", nullable = false)
    Integer bookerId;
    @Transient
    User booker;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    BookingStatus status;

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
}
