package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test4",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    BookingService service;

    @Autowired
    RequestService requestService;

    private Request request;
    private Request request2;
    private Item item1;
    private Item item2;
    private User owner;
    private User booker;
    private Booking booking;
    private Booking booking2;
    int ownerId = 1;
    int itemId1 = 1;
    int itemId2 = 2;

    int bookerId = 2;
    int id = 1;
    int from = 0;
    int size = 20;
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime start = now.plusDays(8);
    LocalDateTime end = now.plusDays(10);

    @BeforeEach
    public void makeBookingsForTests() {

        booking = Booking.builder()
                .id(id)
                .start(start)
                .end(end)
                .itemId(itemId1)
                .item(null)
                .bookerId(bookerId)
                .booker(null)
                .status(BookingStatus.WAITING)
                .build();

        booking2 = Booking.builder()
                .id(2)
                .start(start)
                .end(end)
                .itemId(itemId2)
                .item(null)
                .bookerId(ownerId)
                .booker(null)
                .status(BookingStatus.WAITING)
                .build();

        item1 = Item.builder()
                .id(1)
                .name("item")
                .description("good")
                .available(true)
                .requestId(1)
                .ownerId(ownerId)
                .build();

        item2 = Item.builder()
                .id(2)
                .name("item2")
                .description("veryGood")
                .available(true)
                .requestId(2)
                .ownerId(bookerId)
                .build();

        owner = User.builder()
                .id(ownerId)
                .name("user")
                .email("user@email.ru")
                .build();

        booker = User.builder()
                .id(bookerId)
                .name("user2")
                .email("user2@email.ru")
                .build();

        request = Request.builder()
                .id(1)
                .description("description1")
                .requestorId(bookerId)
                .created(now)
                .build();

        request2 = Request.builder()
                .id(2)
                .description("description1")
                .requestorId(ownerId)
                .created(now)
                .build();
    }

    @Test
    void Test() {
        userService.create(owner);
        userService.create(booker);
        requestService.create(request, bookerId);
        requestService.create(request2, ownerId);
        itemService.create(item1, ownerId);
        itemService.create(item2, bookerId);
        Booking newBooking = service.create(booking, bookerId);
        booking.setItem(null);
        booking.setBooker(null);
        assertThat(newBooking, equalTo(booking));
        booking.setItem(item1);
        booking.setBooker(booker);

        Booking newBooking2 = service.getMyBooking(id, bookerId);
        assertThat(newBooking2, equalTo(booking));

        booking.setStatus(BookingStatus.APPROVED);
        Booking newBooking3 = service.update(id, ownerId, true);
        assertThat(newBooking3, equalTo(booking));

        service.create(booking2, ownerId);
        List<Booking> myBookings = new ArrayList<>();
        myBookings.add(booking2);
        List<Booking> newMyBookings = service.getMyBookings(1, State.ALL, from, size);
        assertThat(myBookings, equalTo(newMyBookings));

        List<Booking> ownerBookings = new ArrayList<>();
        ownerBookings.add(booking2);
        List<Booking> newOwnerBookings = service.getOwnerBookings(2, State.ALL, from, size);
        assertThat(ownerBookings, equalTo(newOwnerBookings));


    }


}
