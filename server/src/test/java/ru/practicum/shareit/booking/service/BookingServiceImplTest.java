package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shareit.booking.model.Booking;
import shareit.booking.model.BookingStatus;
import shareit.booking.model.State;
import shareit.booking.repository.BookingRepository;
import shareit.booking.service.BookingServiceImpl;
import shareit.exceptions.model.OwnerNotFoundException;
import shareit.exceptions.model.ShareItBadRequest;
import shareit.exceptions.model.ShareItNotFoundException;
import shareit.item.model.Item;
import shareit.item.service.ItemService;
import shareit.user.model.User;
import shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @InjectMocks
    BookingServiceImpl bookingService;
    @Mock
    BookingRepository repository;
    @Mock
    UserService userService;
    @Mock
    ItemService itemService;

    private Item item;
    private User owner;
    private User booker;
    private Booking booking;
    private final int ownerId = 1;
    private final int itemId = 1;
    private final int bookerId = 2;
    private final int wrongUserId = 99;
    private final int id = 1;
    private final int from = 0;
    private final int size = 20;
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime start = now.plusDays(8);
    private final LocalDateTime end = now.plusDays(10);

    @BeforeEach
    public void makeBookingForTests() {
        item = Item.builder()
                .id(1)
                .name("item")
                .description("good")
                .available(true)
                .ownerId(ownerId)
                .requestId(1)
                .build();

        owner = User.builder()
                .id(ownerId)
                .name("owner")
                .email("owner@email.ru")
                .build();

        booker = User.builder()
                .id(bookerId)
                .name("booker")
                .email("booker@email.ru")
                .build();

        booking = Booking.builder()
                .id(1)
                .start(start)
                .end(end)
                .itemId(itemId)
                .bookerId(bookerId)
                .status(BookingStatus.WAITING)
                .build();
    }


    @Test
    void createWhenStartIsNullTest() {
        booking.setStart(null);
        assertThrows(
                ShareItBadRequest.class,
                () -> bookingService.create(booking, bookerId));
        Mockito.verifyNoInteractions(repository, userService, itemService);
    }

    @Test
    void createWithoutBookerTest() {
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        Mockito.when(userService.getUserById(wrongUserId)).thenReturn(null);
        assertThrows(
                OwnerNotFoundException.class,
                () -> bookingService.create(booking, wrongUserId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(wrongUserId);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(userService, itemService);
    }

    @Test
    void createWithoutItemTest() {
        Mockito.when(itemService.getItemById(itemId)).thenReturn(null);
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        assertThrows(
                OwnerNotFoundException.class,
                () -> bookingService.create(booking, bookerId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(bookerId);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(userService, itemService);
    }

    @Test
    void createWhenItemIsUnavailableTest() {
        item.setAvailable(false);
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        assertThrows(
                ShareItBadRequest.class,
                () -> bookingService.create(booking, bookerId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(bookerId);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(userService, itemService);
    }

    @Test
    void createWhenStartEqualsEndTest() {
        booking.setStart(end);
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        assertThrows(
                ShareItBadRequest.class,
                () -> bookingService.create(booking, bookerId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(bookerId);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(userService, itemService);
    }

    @Test
    void createWhenStartInFutureTest() {
        booking.setStart(now.minusDays(2));
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        assertThrows(
                ShareItBadRequest.class,
                () -> bookingService.create(booking, bookerId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(bookerId);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(userService, itemService);
    }

    @Test
    void createWhenOwnerAndBookerAreTheSameTest() {
        item.setOwnerId(bookerId);
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        assertThrows(
                OwnerNotFoundException.class,
                () -> bookingService.create(booking, bookerId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(bookerId);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(userService, itemService);
    }

    @Test
    void createTest() {
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        Mockito.when(repository.save(booking)).thenReturn(booking);
        Booking newBooking = bookingService.create(booking, bookerId);
        assertThat(newBooking.equals(booking)).isTrue();
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(bookerId);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(userService, repository, itemService);
    }

    @Test
    void updateBookingNotFoundTest() {
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                ShareItNotFoundException.class,
                () -> bookingService.update(id, bookerId, true));
        Mockito.verify(repository, Mockito.times(1))
                .findById(id);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void updateApproveTest() {
        Booking approved = booking;
        approved.setStatus(BookingStatus.APPROVED);
        booking.setStatus(BookingStatus.WAITING);
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        Mockito.when(repository.save(booking)).thenReturn(booking);
        Booking newBooking = bookingService.update(id, ownerId, true);
        assertThat(newBooking.equals(approved)).isTrue();
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verify(repository, Mockito.times(1))
                .save(booking);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(repository, itemService);
    }

    @Test
    void updateRejectTest() {
        Booking approved = booking;
        approved.setStatus(BookingStatus.REJECTED);
        booking.setStatus(BookingStatus.WAITING);
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        Mockito.when(repository.save(booking)).thenReturn(booking);
        Booking newBooking = bookingService.update(id, ownerId, false);
        assertThat(newBooking.equals(approved)).isTrue();
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verify(repository, Mockito.times(1))
                .save(booking);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(repository, itemService);
    }

    @Test
    void updateAlreadyApprovedTest() {
        booking.setStatus(BookingStatus.APPROVED);
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(booking));
        assertThrows(
                ShareItBadRequest.class,
                () -> bookingService.update(id, bookerId, true));
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void updateWrongOwnerTest() {
        item.setOwnerId(wrongUserId);
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        assertThrows(
                ShareItNotFoundException.class,
                () -> bookingService.update(id, ownerId, true));

        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verifyNoMoreInteractions(itemService, repository);
    }

    @Test
    void updateOwnerIsBookerTest() {
        item.setOwnerId(wrongUserId);
        booking.setBookerId(ownerId);
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        assertThrows(
                OwnerNotFoundException.class,
                () -> bookingService.update(id, ownerId, true));
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(itemService, repository);
    }


    @Test
    void getMyBookingTest() {
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        Booking newBooking = bookingService.getMyBooking(id, bookerId);
        assertThat(newBooking.equals(booking)).isTrue();
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(bookerId);
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(userService, repository, itemService);
    }

    @Test
    void getUnknownBookingTest() {
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                OwnerNotFoundException.class,
                () -> bookingService.getMyBooking(id, bookerId));
        Mockito.verify(repository, Mockito.times(1))
                .findById(id);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void getBookingOfAnotherUserTest() {
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        assertThrows(
                OwnerNotFoundException.class,
                () -> bookingService.getMyBooking(id, wrongUserId));
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(itemService, repository);
    }

    @Test
    void setItemTest() {
        Mockito.when(itemService.getItemById(itemId)).thenReturn(item);
        Booking newBooking = bookingService.setItem(booking);
        assertThat(newBooking.equals(booking)).isTrue();
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void setBookerTest() {
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        Booking newBooking = bookingService.setBooker(booking);
        assertThat(newBooking.equals(booking)).isTrue();
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(bookerId);
        Mockito.verifyNoMoreInteractions(userService);
    }


    @Test
    void getMyBookingsUnknownUserTest() {
        Mockito.when(userService.getUserById(bookerId)).thenReturn(null);
        assertThrows(
                OwnerNotFoundException.class,
                () -> bookingService.getMyBookings(bookerId, State.ALL, from, size));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(bookerId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getAllMyBookingsTest() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        Mockito.when(repository.findMyBookings(bookerId, size, from)).thenReturn(bookings);
        List<Booking> newBookings = bookingService.getMyBookings(bookerId, State.ALL, from, size);
        assertThat(newBookings.equals(bookings)).isTrue();
        Mockito.verify(userService, Mockito.times(2))
                .getUserById(bookerId);
        Mockito.verify(repository, Mockito.times(1))
                .findMyBookings(bookerId, size, from);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void getMyWaitingBookingsTest() {
        booking.setStatus(BookingStatus.WAITING);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        Mockito.when(repository.findMyWaitingBookings(bookerId, size, from)).thenReturn(bookings);
        List<Booking> newBookings = bookingService.getMyBookings(bookerId, State.WAITING, from, size);
        assertThat(newBookings.equals(bookings)).isTrue();

        Mockito.verify(userService, Mockito.times(2))
                .getUserById(bookerId);
        Mockito.verify(repository, Mockito.times(1))
                .findMyWaitingBookings(bookerId, size, from);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void getMyRejectedBookingsTest() {
        booking.setStatus(BookingStatus.REJECTED);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Mockito.when(userService.getUserById(bookerId)).thenReturn(booker);
        Mockito.when(repository.findMyRejectedBookings(bookerId, size, from)).thenReturn(bookings);
        List<Booking> newBookings = bookingService.getMyBookings(bookerId, State.REJECTED, from, size);
        assertThat(newBookings.equals(bookings)).isTrue();
        Mockito.verify(userService, Mockito.times(2))
                .getUserById(bookerId);
        Mockito.verify(repository, Mockito.times(1))
                .findMyRejectedBookings(bookerId, size, from);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }


    @Test
    void getOwnerBookingsUnknownUserTest() {
        Mockito.when(userService.getUserById(ownerId)).thenReturn(null);
        assertThrows(
                OwnerNotFoundException.class,
                () -> bookingService.getOwnerBookings(ownerId, State.ALL, from, size));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(ownerId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getAllOwnerBookingsTest() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Mockito.when(userService.getUserById(ownerId)).thenReturn(owner);
        Mockito.when(repository.findAllByOwnerId(ownerId, from, size)).thenReturn(bookings);
        List<Booking> newBookings = bookingService.getOwnerBookings(ownerId, State.ALL, from, size);
        assertThat(newBookings.equals(bookings)).isTrue();

        Mockito.verify(userService, Mockito.times(1))
                .getUserById(ownerId);
        Mockito.verify(repository, Mockito.times(1))
                .findAllByOwnerId(ownerId, from, size);
        Mockito.verifyNoMoreInteractions(repository);
    }


    @Test
    void getWaitingOwnerBookingsTest() {
        booking.setStatus(BookingStatus.WAITING);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Mockito.when(userService.getUserById(ownerId)).thenReturn(owner);
        Mockito.when(repository.findAllByOwnerIdWaiting(ownerId, from, size)).thenReturn(bookings);
        List<Booking> newBookings = bookingService.getOwnerBookings(ownerId, State.WAITING, from, size);
        assertThat(newBookings.equals(bookings)).isTrue();

        Mockito.verify(userService, Mockito.times(1))
                .getUserById(ownerId);
        Mockito.verify(repository, Mockito.times(1))
                .findAllByOwnerIdWaiting(ownerId, from, size);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void getRejectedOwnerBookingsTest() {
        booking.setStatus(BookingStatus.REJECTED);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Mockito.when(userService.getUserById(ownerId)).thenReturn(owner);
        Mockito.when(repository.findAllByOwnerIdRejected(ownerId, from, size)).thenReturn(bookings);
        List<Booking> newBookings = bookingService.getOwnerBookings(ownerId, State.REJECTED, from, size);
        assertThat(newBookings.equals(bookings)).isTrue();
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(ownerId);
        Mockito.verify(repository, Mockito.times(1))
                .findAllByOwnerIdRejected(ownerId, from, size);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId);
        Mockito.verifyNoMoreInteractions(repository);
    }
}
