package shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shareit.booking.model.Booking;
import shareit.booking.model.BookingStatus;
import shareit.booking.model.State;
import shareit.booking.repository.BookingRepository;
import shareit.exceptions.model.OwnerNotFoundException;
import shareit.exceptions.model.ShareItBadRequest;
import shareit.exceptions.model.ShareItNotFoundException;
import shareit.item.model.Item;
import shareit.item.service.ItemService;
import shareit.user.model.User;
import shareit.user.service.UserService;

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

        if (booking.getStart() == null || booking.getEnd() == null) {
            throw new ShareItBadRequest("time is null");
        }
        Item item = itemService.getItemById(booking.getItemId());
        User booker = userService.getUserById(bookerId);
        if (booker == null) {
            throw new OwnerNotFoundException("Booker with Id " + bookerId + " not found");
        }
        if (item == null) {
            throw new OwnerNotFoundException("Item with Id " + booking.getItemId() + " not found");
        }
        if (!item.getAvailable()) {
            throw new ShareItBadRequest("Item with Id " + booking.getItemId() + " is not available");
        }
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().equals(booking.getEnd())) {
            throw new ShareItBadRequest("start: " + booking.getStart() + " is after end: " + booking.getEnd());
        }
        if (booking.getEnd().isBefore(LocalDateTime.now()) || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ShareItBadRequest("time is in the past");
        }
        if (item.getOwnerId() == bookerId) {
            throw new OwnerNotFoundException("The same owner and booker with Id " + bookerId);
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
            if (booking.getStatus() == BookingStatus.WAITING) {
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
                    throw new OwnerNotFoundException("OwnerId: " + ownerId + " совпадает с bookerId");
                } else {
                    throw new ShareItNotFoundException("неверный ownerId: " + ownerId);
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
                throw new OwnerNotFoundException("Это не ваша вещь: " + item);
            }
        } else {
            throw new OwnerNotFoundException("Booking с Id: " + id + " не найден");
        }
    }

    @Override
    public List<Booking> getMyBookings(int userId, State state, int from, int size) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("Owner with Id " + userId + " not found");
        }
        switch (state) {
            case ALL:
                return repository.findMyBookings(userId, size, from).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            case FUTURE:
                return repository.findMyFutureBookings(userId, LocalDateTime.now(), size, from).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            case PAST:
                return repository.findMyPastBookings(userId, LocalDateTime.now(), size, from).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            case CURRENT:
                return repository.findMyCurrentBookings(userId,
                                LocalDateTime.now(), size, from).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            case WAITING:
                return repository.findMyWaitingBookings(userId, size, from).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            case REJECTED:
                return repository.findMyRejectedBookings(userId, size, from).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public List<Booking> getOwnerBookings(int userId, State state, int from, int size) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("Owner with Id " + userId + " not found");
        }
        switch (state) {
            case ALL:
                return repository.findAllByOwnerId(userId, from, size).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            case FUTURE:
                return repository.findAllByOwnerIdFuture(userId, LocalDateTime.now(), from, size).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            case PAST:
                return repository.findAllByOwnerIdPast(userId, LocalDateTime.now(), from, size).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            case CURRENT:
                return repository.findAllByOwnerIdNow(userId,
                                LocalDateTime.now(), from, size).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());

            case WAITING:
                return repository.findAllByOwnerIdWaiting(userId, from, size).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            case REJECTED:
                return repository.findAllByOwnerIdRejected(userId, from, size).stream()
                        .map(this::setItem).map(this::setBooker)
                        .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
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
