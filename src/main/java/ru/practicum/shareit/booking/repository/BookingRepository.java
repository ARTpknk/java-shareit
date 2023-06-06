package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findBookingsByBookerIdOrderByStartDesc(Integer bookerId);

    List<Booking> findAllByBookerIdAndStatus(Integer bookerId, BookingStatus status);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Integer bookerId, LocalDateTime start);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Integer bookerId, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Integer bookerId, LocalDateTime start,
                                                                             LocalDateTime end);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE owner_id = ? ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerId(Integer ownerId);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE owner_id = ?1 AND start_date > ?2  ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdFuture(Integer ownerId, LocalDateTime now);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE owner_id = ?1 AND end_date < ?2  ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdPast(Integer ownerId, LocalDateTime now);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE owner_id = ?1 AND start_date < ?2 AND end_date > ?2 ORDER BY start_date", nativeQuery = true)
    List<Booking> findAllByOwnerIdNow(Integer ownerId, LocalDateTime now);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE owner_id = ?1 AND status = 'REJECTED'  ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdRejected(Integer ownerId);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE owner_id = ?1 AND status = 'WAITING'  ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdWaiting(Integer ownerId);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.id = ?1 AND start_date > ?2 AND status = 'APPROVED' AND owner_id = ?3  ORDER BY start_date LIMIT 1", nativeQuery = true)
    Booking findNextBooking(Integer itemId, LocalDateTime now, Integer ownerId);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.id = ?1 AND start_date < ?2 AND owner_id = ?3  ORDER BY start_date DESC LIMIT 1", nativeQuery = true)
    Booking findLastBooking(Integer itemId, LocalDateTime now, Integer ownerId);

    @Query(value = "SELECT * FROM Bookings AS b " +
            "WHERE item_id = ?1 AND booker_id = ?2 AND status = 'APPROVED' AND start_date < ?3", nativeQuery = true)
    List<Booking> findUsedBookings(Integer itemId, Integer bookerId, LocalDateTime time);

}
