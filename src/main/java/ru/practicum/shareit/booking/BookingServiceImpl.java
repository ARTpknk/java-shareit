package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.OwnerNotFoundException;
import ru.practicum.shareit.exceptions.model.ShareItBadRequest;
import ru.practicum.shareit.exceptions.model.ShareItNotFoundException;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final UserService userService;
    private final ItemService itemService;


    @Override
    public Booking create(Booking booking, int bookerId) {
        try {
            if (booking.getStart() == null || booking.getEnd() == null) {
                throw new ShareItBadRequest("time is null");
            }
        } catch (NullPointerException e) {
            throw new ShareItBadRequest("time is null");
        }
        Item item = itemService.getItemById(booking.getItemId());
        User booker = userService.getUserById(bookerId);
        if (booker == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        if (item == null) {
            throw new OwnerNotFoundException("Item not found");
        }
        if (!item.getAvailable()) {
            throw new ShareItBadRequest("item is not available");
        }
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().equals(booking.getEnd())) {
            throw new ShareItBadRequest("start is after end");
        }
        if (booking.getEnd().isBefore(LocalDateTime.now()) || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ShareItBadRequest("time is in the past");
        }
        if (item.getOwnerId() == bookerId) {
            throw new OwnerNotFoundException("The same owner and booker");
        }

        booking.setBookerId(bookerId);
        booking.setItem(item);
        booking.setBooker(booker);
        return repository.save(booking);
    }

    @Override
    public Booking update(int id, int ownerId, boolean approved) {
        if (repository.findById(id).isPresent()) {
            Booking booking = repository.findById(id).get();
            if (booking.status == BookingStatus.WAITING) {
                Item item = itemService.getItemById(booking.getItemId());
                if (item.getOwnerId() == ownerId) {
                    if (approved) {
                        booking.setStatus(BookingStatus.APPROVED);
                    } else {
                        booking.setStatus(BookingStatus.REJECTED);
                    }
                    repository.save(booking);
                    booking.setItem(item);
                    booking.setBooker(userService.getUserById(booking.getBookerId()));
                    return booking;
                } else if (ownerId == booking.getBookerId()) {
                    throw new OwnerNotFoundException("неверный ownerId");
                } else {
                    throw new ShareItNotFoundException("неверный ownerId");
                }
            } else {
                throw new ShareItBadRequest("уже получен ответ");
            }
        } else {
            throw new ShareItNotFoundException(" Booking не найден");
        }
    }


    public Booking getMyBooking(int id, int userId) {
        if (repository.findById(id).isPresent()) {
            Booking booking = repository.findById(id).get();
            Item item = itemService.getItemById(booking.getItemId());
            booking.setItem(item);
            booking.setBooker(userService.getUserById(booking.getBookerId()));
            if (userId == booking.getBookerId() || userId == item.getOwnerId()) {
                return booking;
            } else {
                throw new OwnerNotFoundException("Это не ваша вещь");
            }
        } else {
            throw new OwnerNotFoundException("Booking не найден");
        }
    }

    @Override
    public List<Booking> getMyBookings(int userId, State state) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        if (state == State.ALL) {
            return repository.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }
        if (state == State.FUTURE) {
            return repository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }
        if (state == State.PAST) {
            return repository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }
        if (state == State.CURRENT) {
            return repository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now()).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }
        if (state == State.WAITING) {
            return repository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }
        if (state == State.REJECTED) {
            return repository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Booking> getOwnerBookings(int userId, State state) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        if (state == State.ALL) {
            return repository.findAllByOwnerId(userId).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }

        if (state == State.FUTURE) {
            return repository.findAllByOwnerIdFuture(userId, LocalDateTime.now()).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }

        if (state == State.PAST) {
            return repository.findAllByOwnerIdPast(userId, LocalDateTime.now()).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }
        if (state == State.CURRENT) {
            return repository.findAllByOwnerIdNow(userId,
                            LocalDateTime.now()).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }

        if (state == State.WAITING) {
            return repository.findAllByOwnerIdWaiting(userId).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }
        if (state == State.REJECTED) {
            return repository.findAllByOwnerIdRejected(userId).stream()
                    .map(this::setItem).map(this::setBooker)
                    .collect(Collectors.toList());
        }




















        return Collections.emptyList();
    }

    public Booking setItem(Booking booking) {
        booking.setItem(itemService.getItemById(booking.getItemId()));
        return booking;
    }

    public Booking setBooker(Booking booking) {
        booking.setBooker(userService.getUserById(booking.getBookerId()));
        return booking;
    }

}
