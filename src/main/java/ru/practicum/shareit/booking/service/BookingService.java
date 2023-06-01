package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    Booking create(Booking booking, int bookerId);

    Booking update(int id, int bookerId, boolean approved);

    Booking getMyBooking(int id, int userId);

    List<Booking> getMyBookings(int userId, State state);

    List<Booking> getOwnerBookings(int userId, State state);
}
