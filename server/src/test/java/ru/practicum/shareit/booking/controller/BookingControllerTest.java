package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shareit.booking.dto.BookingDto;
import shareit.booking.model.Booking;
import shareit.booking.model.BookingStatus;
import shareit.booking.model.State;
import shareit.booking.service.BookingService;
import shareit.item.controller.ItemController;
import shareit.item.service.ItemService;
import shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BookingControllerTest {
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @InjectMocks
    private ItemController controller;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    private Booking booking;
    private Booking booking2;
    private BookingDto bookingDto;
    private BookingDto bookingDto2;
    private final int from = 0;
    private final int size = 20;
    private final LocalDateTime start = LocalDateTime.of(2023,
            Month.JUNE, 10, 16, 14, 19, 99);
    private final LocalDateTime end = LocalDateTime.of(2023,
            Month.JUNE, 20, 16, 14, 19, 99);

    @BeforeEach
    void setUp() {
        int bookerId = 2;
        int itemId = 1;

        booking = Booking.builder()
                .id(1)
                .start(start)
                .end(end)
                .itemId(itemId)
                .bookerId(bookerId)
                .status(BookingStatus.WAITING)
                .build();

        booking2 = Booking.builder()
                .id(2)
                .start(start)
                .end(end)
                .itemId(itemId)
                .bookerId(bookerId)
                .status(BookingStatus.WAITING)
                .build();

        bookingDto = BookingDto.builder()
                .id(1)
                .start(start)
                .end(end)
                .itemId(itemId)
                .status(BookingStatus.WAITING)
                .build();

        bookingDto2 = BookingDto.builder()
                .id(2)
                .start(start)
                .end(end)
                .itemId(itemId)
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void createTest() throws Exception {
        when(bookingService.create(any(), anyInt()))
                .thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class));
    }

    @Test
    void updateTest() throws Exception {
        when(bookingService.update(1, 1, true))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class));
    }

    @Test
    void getMyBookingTest() throws Exception {
        when(bookingService.getMyBooking(1, 1))
                .thenReturn(booking);

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class));
    }

    @Test
    void getMyBookingsTest() throws Exception {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        when(bookingService.getMyBookings(1, State.ALL, from, size))
                .thenReturn(bookings);

        mvc.perform(get("/bookings", 1)
                        .content(objectMapper.writeValueAsString(bookings))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[1].id").value(bookingDto2.getId()))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getMyBookingsWrongFromTest() throws Exception {


        mvc.perform(get("/bookings", 1)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .param("from", String.valueOf(-1))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getOwnerBookingsWrongFromTest() throws Exception {
        mvc.perform(get("/bookings/owner", 1)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .param("from", String.valueOf(-1))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void getOwnerBookingsTest() throws Exception {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        when(bookingService.getOwnerBookings(1, State.ALL, from, size))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner", 1)
                        .content(objectMapper.writeValueAsString(bookings))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[1].id").value(bookingDto2.getId()))
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
