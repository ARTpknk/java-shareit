package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.user.dto.User;

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
    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE owner_id = ? ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerId(Integer ownerId);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE owner_id = ?1 AND start_date > ?2  ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdFuture(Integer ownerId, LocalDateTime now);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE owner_id = ?1 AND end_date < ?2  ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdPast(Integer ownerId, LocalDateTime now);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE owner_id = ?1 AND start_date < ?2 AND end_date > ?2  ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdNow(Integer ownerId, LocalDateTime now);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE owner_id = ?1 AND status = 'REJECTED'  ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdRejected(Integer ownerId);

    @Query(value = "SELECT * FROM Bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE owner_id = ?1 AND status = 'WAITING'  ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdWaiting(Integer ownerId);





}
