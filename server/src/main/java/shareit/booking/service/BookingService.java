package shareit.booking.service;

import shareit.booking.model.Booking;
import shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    Booking create(Booking booking, int bookerId);

    Booking update(int id, int bookerId, boolean approved);

    Booking getMyBooking(int id, int userId);

    List<Booking> getMyBookings(int userId, State state, int from, int size);

    List<Booking> getOwnerBookings(int userId, State state, int from, int size);
}
